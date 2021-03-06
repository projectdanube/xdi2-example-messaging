package xdi2.example.messaging;

import xdi2.client.XDIClient;
import xdi2.client.impl.local.XDILocalClient;
import xdi2.core.Graph;
import xdi2.core.impl.memory.MemoryGraphFactory;
import xdi2.core.io.XDIReader;
import xdi2.core.io.XDIReaderRegistry;
import xdi2.core.io.XDIWriter;
import xdi2.core.io.XDIWriterRegistry;
import xdi2.core.syntax.XDIAddress;
import xdi2.messaging.Message;
import xdi2.messaging.MessageCollection;
import xdi2.messaging.MessageEnvelope;
import xdi2.messaging.container.impl.graph.GraphMessagingContainer;
import xdi2.messaging.response.MessagingResponse;

public class SimpleMessagingSample {

    public static void main(String[] args) throws Exception {

        XDIReader reader = XDIReaderRegistry.forFormat("XDI DISPLAY", null);
        XDIWriter writer = XDIWriterRegistry.forFormat("XDI/JSON", null);

        // load an XDI graph and create a messaging container

        Graph graph = MemoryGraphFactory.getInstance().openGraph();
        reader.read(graph, SimpleMessagingSample.class.getResourceAsStream("simple.xdi"));
        GraphMessagingContainer graphContainer = new GraphMessagingContainer();
        graphContainer.setGraph(graph);

        // create a message

        MessageEnvelope messageEnvelope = new MessageEnvelope();
        MessageCollection messageCollection = messageEnvelope.getMessageCollection(XDIAddress.create("=sender"), true);
        Message message = messageCollection.createMessage();
        message.createGetOperation(XDIAddress.create("=markus"));

        // execute the message

        XDIClient xdiClient = new XDILocalClient(graphContainer);
        MessagingResponse messagingResponse = xdiClient.send(messageEnvelope);

        // serialize the result

        writer.write(messagingResponse.getGraph(), System.out);
    }
}