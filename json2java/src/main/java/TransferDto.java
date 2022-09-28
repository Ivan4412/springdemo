import java.util.List;

public class TransferDto {
    public String from;
    public String to;
    public List<Result> trans_result;

    public static class Result {
        public String src;
        public String dst;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDst() {
            return dst;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<Result> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<Result> trans_result) {
        this.trans_result = trans_result;
    }
}
