package EncoderTest;

import com.google.protobuf.CodedOutputStream;
import ming.protobuf.TradeMessages;
import ming.protobuf.TradeMessagesV2;

import java.nio.ByteBuffer;
import java.util.Objects;

public class TestProtobuf {

    public static double run() throws Exception {
        int time = 0;
        ByteBuffer buf = ByteBuffer.allocate(10000);
        for (int j = 0; j < 10_000 ; j++) {
            runTest(buf);
        }

        System.gc();
        long start = System.nanoTime();
        for (int j = 0; j < 10_000_000 ; j++) {
            runTest(buf);
        }
        long end = System.nanoTime();
        return (end - start) * 1.0/1_000_000_000;
    }

    private static void runTest(ByteBuffer buf) throws java.io.IOException {
        TradeMessagesV2.NewOrder.Builder v2 = TradeMessagesV2.NewOrder.newBuilder();
        CodedOutputStream outputStream = CodedOutputStream.newInstance(buf);
        v2.setClOrdId("12345");
        v2.setPx(1000);
        v2.setQty(1000);
        v2.setAccount("abcde");
        v2.setSide(TradeMessagesV2.Side.BUY);
        v2.setOrdType(TradeMessagesV2.OrdType.MKT);
        v2.build().writeTo(outputStream);
        outputStream.flush();

        int hashOld = v2.getClOrdId().hashCode();
        hashOld += v2.getPx();
        hashOld += v2.getQty();
        hashOld += v2.getSide().toString().hashCode();
        hashOld += v2.getOrdType().toString().hashCode();

        buf.flip();
        TradeMessages.NewOrder vOrder = TradeMessages.NewOrder.parseFrom(buf);
        buf.position(buf.limit());
        buf.compact();
        int hashNew = vOrder.getClOrdId().hashCode();
        hashNew += vOrder.getPx();
        hashNew += vOrder.getQty();
        hashNew += vOrder.getSide().toString().hashCode();
        hashNew += vOrder.getOrdType().toString().hashCode();
        if(hashOld != hashNew){
            throw new RuntimeException("XXX!!");
        }
    }
}
