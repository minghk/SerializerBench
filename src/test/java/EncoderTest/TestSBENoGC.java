package EncoderTest;

import com.example.sbe.NewOrderSingleDecoder;
import com.example.sbe2.OrdTypeEnum;
import com.example.sbe2.SideEnum;
import org.agrona.ExpandableDirectByteBuffer;

import java.util.Arrays;
import java.util.Objects;

public class TestSBENoGC {

    public static double run() throws Exception{
        ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1000);

        com.example.sbe2.MessageHeaderEncoder headerEncoder2 = new com.example.sbe2.MessageHeaderEncoder();
        com.example.sbe2.NewOrderSingleEncoder orderSingleEncoder2  = new com.example.sbe2.NewOrderSingleEncoder();

        com.example.sbe.MessageHeaderDecoder headerDecoder = new com.example.sbe.MessageHeaderDecoder();
        NewOrderSingleDecoder orderSingleDecoder  = new NewOrderSingleDecoder();

        byte[] clOrdIdValue = new byte[com.example.sbe2.NewOrderSingleEncoder.clOrdIdEncodingLength()];
        Arrays.fill(clOrdIdValue,(byte)0);
        System.arraycopy("12345".getBytes(), 0, clOrdIdValue, 0, 5);

        byte[] clOrdBuff = new byte[com.example.sbe2.NewOrderSingleEncoder.clOrdIdEncodingLength()];
        Arrays.fill(clOrdBuff,(byte)0);

        int hashOld = Objects.hash(
                Arrays.hashCode(clOrdIdValue),
                1000, 1000, SideEnum.Buy.toString(), "Limit");

        for (int j = 0; j < 10000 ; j++) {
            runTest(buffer, headerEncoder2, orderSingleEncoder2, headerDecoder, orderSingleDecoder, clOrdIdValue, clOrdBuff, hashOld);
        }

        System.gc();

        long start = System.nanoTime();
        for (int j = 0; j < 10000000 ; j++) {
            runTest(buffer, headerEncoder2, orderSingleEncoder2, headerDecoder, orderSingleDecoder, clOrdIdValue, clOrdBuff, hashOld);

        }
        long end = System.nanoTime();
        System.out.println("Cost = " + (end - start) * 1.0/1_000_000_000);
        return (end - start) * 1.0/1_000_000_000;
    }

    private static void runTest(ExpandableDirectByteBuffer buffer, com.example.sbe2.MessageHeaderEncoder headerEncoder2, com.example.sbe2.NewOrderSingleEncoder orderSingleEncoder2, com.example.sbe.MessageHeaderDecoder headerDecoder, NewOrderSingleDecoder orderSingleDecoder, byte[] clOrdIdValue, byte[] clOrdBuff, int hashOld) {
        orderSingleEncoder2.wrapAndApplyHeader(buffer, 0, headerEncoder2);

        orderSingleEncoder2.putClOrdId(clOrdIdValue,0);
        orderSingleEncoder2.price(1000);
        orderSingleEncoder2.ordQty(1000);
        orderSingleEncoder2.side(SideEnum.Buy);
        orderSingleEncoder2.ordType(OrdTypeEnum.Limit);

        headerDecoder.wrap(buffer, 0);
        orderSingleDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());

        orderSingleDecoder.getClOrdId(clOrdBuff,0);

        int hashNew = Objects.hash(
                Arrays.hashCode(clOrdBuff),
                orderSingleDecoder.price(), orderSingleDecoder.ordQty(), orderSingleDecoder.side().toString(), orderSingleDecoder.ordType().toString());

        if(hashOld != hashNew){
            throw new RuntimeException("XXX!!");
        }
    }
}
