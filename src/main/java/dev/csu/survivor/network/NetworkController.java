package dev.csu.survivor.network;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.net.Server;
import dev.csu.survivor.Constants;
import dev.csu.survivor.input.OnlineGameInput;
import dev.csu.survivor.util.ValidationUtil;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkController
{
    private boolean isServer;
    private Server<Bundle> server;
    private String ipAddress;
    private static Connection<Bundle> connection;
    private boolean isOnConnection;

    public void init()
    {
        FXGL.runOnce(() -> FXGL.getDialogService().showConfirmationBox("Are you the host?", answer -> {
            isServer = answer;

            if (isServer) {
                try {
                    if (!hostServerPortAvailabilityCheck(Constants.SERVER_PORT)) {
                        throw new RuntimeException();
                    }

                    server = FXGL.getNetService().newTCPServer(Constants.SERVER_PORT);

                    server.startAsync();
                } catch (RuntimeException | IOException e) {
                    FXGL.getDialogService().showErrorBox("Can't create new host! Server is already hosting.", this::connectClient);
                }
            } else {
                connectClient();
            }
        }), Duration.seconds(0));
    }

    public void onUpdate(double tpf)
    {
        if (isServer && server != null)
        {
            if (!isOnConnection && !server.getConnections().isEmpty()) onServer();
            else if (server.getConnections().isEmpty())
            {
                isOnConnection = false;

//                getDialogService().showChoiceBox("test", s->
//                {
//                    FXGL.getSceneService().popSubScene();
//                });
            }
        }
    }

    private void connectClient()
    {
        FXGL.getDialogService().showInputBox("Enter Server IP Address to connect as client", answer -> {
            try {
                ipAddress = ValidationUtil.validateIP(answer);
                InetAddress address = InetAddress.getByName(ipAddress);
                boolean reachable = address.isReachable(2000);

                if (!reachable || !hostPortAvailabilityCheck(ipAddress, Constants.SERVER_PORT)) {
                    throw new RuntimeException("Server IP Address not found. Try Again!");
                }

                var client = FXGL.getNetService().newTCPClient(ipAddress, Constants.SERVER_PORT);
                client.setOnConnected(conn -> {
                    connection = conn;

                    FXGL.getExecutor().startAsyncFX(this::onClient);
                });

                client.connectAsync();
            } catch (IllegalArgumentException e) {
                FXGL.getDialogService().showErrorBox("Black listed character have been found. Invalid IP address.", this::connectClient);
            } catch (RuntimeException e) {
                FXGL.getDialogService().showErrorBox("Server IP Address is not currently hosting. Try Again!", this::connectClient);
            } catch (IOException ex) {
                FXGL.getDialogService().showErrorBox("Unreachable Host/Invalid Input. Try Again!", this::connectClient);
            }
        });
    }

    // checks if port on selected ipAddress is available
    public boolean hostPortAvailabilityCheck(String ipAddress, int port) throws IOException {
        try (Socket socket = new Socket(ipAddress, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // checks if server's port is available
    public boolean hostServerPortAvailabilityCheck(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void onServer()
    {
        connection = server.getConnections().getFirst();

        new OnlineGameInput().initServerInput(FXGL.getInput(), connection);

        connection.addMessageHandlerFX((connection, message) -> {
            if (message.getName().equals("spawn_enemy"))
            {
                Vec2 vec2 = message.get("spawn_position");
                FXGL.getGameWorld().spawn("enemy", vec2.x, vec2.y);
            }
        });

        isOnConnection = true;
    }

    private void onClient()
    {
        new OnlineGameInput().initClientInput(FXGL.getInput(), connection);

        connection.addMessageHandlerFX((connection, message) -> {
        });

        isOnConnection = true;
    }
}
