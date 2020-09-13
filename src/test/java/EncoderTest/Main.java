package EncoderTest;

import static EncoderTest.TestJavaSerialize.runTest;

public class Main {
    public static void main(String[] args) throws Exception {
        double avg = 0;
        double javaSerializer = runTest();
        avg = javaSerializer / 10000000 * 1_000_000;
        System.out.println("JavaSerialize Total(sec):" + javaSerializer + " Avg(microsecond):" + avg);

        double protobuf = TestProtobuf.run();
        avg = protobuf / 10000000 * 1_000_000;
        System.out.println("ProtoBuf Total(sec):" + protobuf + " Avg(microsecond):" + avg);
//
        double sbe = TestSBE.run();
        avg = sbe / 10000000 * 1_000_000;
        System.out.println("SBE Normal Total(sec):" + sbe + " Avg(microsecond):" + avg);

        double sbeNoGC = TestSBENoGC.run();
        avg = sbeNoGC / 10000000 * 1_000_000;
        System.out.println("SBE NoGC Total(sec):" + sbeNoGC + " Avg(microsecond):" + avg);
    }
}
