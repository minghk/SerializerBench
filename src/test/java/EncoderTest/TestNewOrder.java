package EncoderTest;

import java.io.Serializable;
import java.util.Objects;

enum Side{
    BUY,
    SELL
}

enum OrdType{
    MARKET,
    LIMIT
}



public class TestNewOrder implements Serializable {

    private String clOrdId;
    private String symbol;
    private long px;
    private long qty;
    private String dest;
    private OrdType ordType;
    private Side side;

    public String getClOrdId() {
        return clOrdId;
    }

    public void setClOrdId(String clOrdId) {
        this.clOrdId = clOrdId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getPx() {
        return px;
    }

    public void setPx(long px) {
        this.px = px;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public OrdType getOrdType() {
        return ordType;
    }

    public void setOrdType(OrdType ordType) {
        this.ordType = ordType;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return "TestNewOrder{" +
                "clOrdId='" + clOrdId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", px=" + px +
                ", qty=" + qty +
                ", dest='" + dest + '\'' +
                ", ordType=" + ordType +
                ", side=" + side +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestNewOrder order = (TestNewOrder) o;
        return px == order.px &&
                qty == order.qty &&
                Objects.equals(clOrdId, order.clOrdId) &&
                Objects.equals(symbol, order.symbol) &&
                Objects.equals(dest, order.dest) &&
                ordType == order.ordType &&
                side == order.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clOrdId, symbol, px, qty, dest, ordType, side);
    }
}
