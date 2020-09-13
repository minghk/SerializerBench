package EncoderTest;

import java.io.*;

public class TestJavaSerialize {
    public static double runTest() throws Exception{
        double result = 0;
        TestNewOrder order = new TestNewOrder();
        System.gc();
        order.setClOrdId("12345");
        order.setSymbol("12345");
        order.setQty(1000);
        order.setDest("2321");
        order.setPx(1000);
        order.setSide(Side.BUY);
        order.setOrdType(OrdType.LIMIT);



        long h = order.hashCode();
        for (int j = 0; j < 10000 ; j++) {
            runTest(order, h);
        }


        long start = System.nanoTime();
        for (int j = 0; j < 10000000 ; j++) {
            runTest(order, h);
        }
        long end = System.nanoTime();

        result = (end - start) * 1.0/1_000_000_000;
        System.gc();
        return result;
    }

    private static void runTest(TestNewOrder order, long h) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1000);
        ObjectOutputStream out = new ObjectOutputStream(outputStream);

        out.writeObject(order);
        out.close();
        outputStream.close();
        byte[] a = outputStream.toByteArray();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(a));
        int o = in.readObject().hashCode();
        if(o != h){
            throw new RuntimeException("ABC");
        }
    }

    public static void main(String[] args) throws Exception {
        double avg = 0;
        double javaSerializer = runTest();
        avg = javaSerializer / 10000000 * 1_000_000;
        System.out.println("JavaSerialize:" + javaSerializer + " Avg(microsecond):" + avg);

        double protobuf = TestProtobuf.run();
        avg = protobuf / 10000000 * 1_000_000;
        System.out.println("ProtoBuf:" + protobuf + " Avg(microsecond):" + avg);

        double sbe = TestSBE.run();
        avg = sbe / 10000000 * 1_000_000;
        System.out.println("SBE Normal:" + sbe + " Avg(microsecond):" + avg);

        double sbeNoGC = TestSBENoGC.run();
        avg = sbeNoGC / 10000000 * 1_000_000;
        System.out.println("SBE NoGC:" + sbe + " Avg(microsecond):" + avg);
    }
}
