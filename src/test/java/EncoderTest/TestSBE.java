package EncoderTest;

import com.example.sbe.NewOrderSingleDecoder;
import com.example.sbe2.OrdTypeEnum;
import com.example.sbe2.SideEnum;
import com.google.protobuf.CodedOutputStream;
import ming.protobuf.TradeMessages;
import ming.protobuf.TradeMessagesV2;
import org.agrona.ExpandableDirectByteBuffer;

import java.nio.ByteBuffer;
import java.util.Objects;

public class TestSBE {

    public static double run() throws Exception{
        ByteBuffer buf = ByteBuffer.allocate(10000);
        ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1000);

        com.example.sbe2.MessageHeaderEncoder headerEncoder2 = new com.example.sbe2.MessageHeaderEncoder();
        com.example.sbe2.NewOrderSingleEncoder orderSingleEncoder2  = new com.example.sbe2.NewOrderSingleEncoder();

        com.example.sbe.MessageHeaderDecoder headerDecoder = new com.example.sbe.MessageHeaderDecoder();
        com.example.sbe.NewOrderSingleDecoder orderSingleDecoder  = new com.example.sbe.NewOrderSingleDecoder();

        int hashOld = Objects.hash("12345", 1000L, 1000L, SideEnum.Buy.toString(), OrdTypeEnum.Limit.toString());

        for (int j = 0; j < 10000 ; j++) {
            runTest(buffer, headerEncoder2, orderSingleEncoder2, headerDecoder, orderSingleDecoder, hashOld);
        }

        System.gc();
        long start = System.nanoTime();
        for (int j = 0; j < 10000000 ; j++) {
            runTest(buffer, headerEncoder2, orderSingleEncoder2, headerDecoder, orderSingleDecoder, hashOld);
        }
        long end = System.nanoTime();
        return (end - start) * 1.0/1_000_000_000;

    }

    private static void runTest(ExpandableDirectByteBuffer buffer, com.example.sbe2.MessageHeaderEncoder headerEncoder2, com.example.sbe2.NewOrderSingleEncoder orderSingleEncoder2, com.example.sbe.MessageHeaderDecoder headerDecoder, NewOrderSingleDecoder orderSingleDecoder, int hashOld) {
        orderSingleEncoder2.wrapAndApplyHeader(buffer, 0, headerEncoder2);

        orderSingleEncoder2.clOrdId("12345");
        orderSingleEncoder2.price(1000);
        orderSingleEncoder2.ordQty(1000);
        orderSingleEncoder2.account("12345");
        orderSingleEncoder2.side(SideEnum.Buy);
        orderSingleEncoder2.ordType(OrdTypeEnum.Limit);

        headerDecoder.wrap(buffer, 0);
        orderSingleDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());

        int hashNew = Objects.hash(
                orderSingleDecoder.clOrdId(),
                 orderSingleDecoder.price(), orderSingleDecoder.ordQty(), orderSingleDecoder.side().toString(), orderSingleDecoder.ordType().toString());


        if(hashOld != hashNew){
            throw new RuntimeException("XXX!!");
        }
    }
}
