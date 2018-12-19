package cdictv.news.Been;

import java.util.List;

public class MessBeen {
    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2018-10-11 00:00","title":"黑龙江\u201c反杀案\u201d：男子遭刺夺刀反杀 赔40万获刑6年","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p1.ifengimg.com/2018_41/95EBE69693710723C00D6A9249526398D6CA197D_w550_h536.jpg","url":"http://news.ifeng.com/a/20181011/60103076_0.shtml"},{"ctime":"2018-10-11 00:00","title":"警方通报\u201c三男打一女\u201d调查结果：初步确定涉嫌犯罪","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p0.ifengimg.com/pmop/2018/1011/E2FC09CBA0E458A57453930A926036C16DC8620A_size19_w562_h309.jpeg","url":"http://news.ifeng.com/a/20181011/60102981_0.shtml"},{"ctime":"2018-10-11 00:00","title":"南北方6大遗传差异首次被揭示","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p0.ifengimg.com/pmop/2018/1010/38E560FCB65842C8BE25A8170D07A49F85B9636A_size96_w600_h429.jpeg","url":"http://news.ifeng.com/a/20181011/60102993_0.shtml"},{"ctime":"2018-10-11 00:00","title":"滴滴回应乘客喝到尿：司机用矿泉水瓶方便致误饮","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p0.ifengimg.com/2018_41/D9739A61448A03F3707939AC87C5301FF88F6778_w580_h394.jpg","url":"http://news.ifeng.com/a/20181011/60102963_0.shtml"},{"ctime":"2018-10-11 00:00","title":"女律师自曝遭\u201c碰瓷\u201d执法事件 不妨公开视频平息争议","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p3.ifengimg.com/2018_41/CE24AEC59BCA429053C1D158B7782D7116E15BB7_w1080_h1920.jpg","url":"http://news.ifeng.com/a/20181011/60103157_0.shtml"},{"ctime":"2018-10-11 00:00","title":"10岁女孩卖水果回家途中遇害 疑遭性侵后被藏尸草丛","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p0.ifengimg.com/pmop/2018/1011/C3AAD91F570A3B359FDA00897332CE0EE15EB09B_size194_w600_h338.jpeg","url":"http://news.ifeng.com/a/20181011/60103135_0.shtml"},{"ctime":"2018-10-11 00:00","title":"宁波一男子因狗吠影响睡眠 持刀捅向邻居致3死1伤","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p0.ifengimg.com/pmop/2018/1011/23F343203604D97BCE4E7885E81B8EB8FA867642_size496_w642_h1292.jpeg","url":"http://news.ifeng.com/a/20181011/60103093_0.shtml"},{"ctime":"2018-10-11 00:00","title":"大一女生患白血病被要求退学 校方：退学符合规定","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p0.ifengimg.com/2018_41/60918FDBB10B6F961E8D0CA09BB4FCA17AF88EB5_w373_h600.jpg","url":"http://news.ifeng.com/a/20181011/60103363_0.shtml"},{"ctime":"2018-10-11 00:00","title":"15岁女孩被强奸警方不立案？警方通报：严重失实","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p0.ifengimg.com/pmop/2018/1011/BA4C171521D080D4348B85A2DFF5F8AB4C298985_size35_w600_h429.jpeg","url":"http://news.ifeng.com/a/20181011/60103358_0.shtml"},{"ctime":"2018-10-11 00:00","title":"\u201c他们拿小老虎拍照，它在惨叫\u201d","description":"凤凰社会","picUrl":"http://d.ifengimg.com/w150_h95/p0.ifengimg.com/pmop/2018/1011/98FF687C2DAA097904AB6329B918074C2B1ADBF3_size98_w540_h357.png","url":"http://news.ifeng.com/a/20181011/60103333_0.shtml"}]
     */

    private int code;
    private String msg;
    private List<NewslistBean> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewslistBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

    public static class NewslistBean {
        /**
         * ctime : 2018-10-11 00:00
         * title : 黑龙江“反杀案”：男子遭刺夺刀反杀 赔40万获刑6年
         * description : 凤凰社会
         * picUrl : http://d.ifengimg.com/w150_h95/p1.ifengimg.com/2018_41/95EBE69693710723C00D6A9249526398D6CA197D_w550_h536.jpg
         * url : http://news.ifeng.com/a/20181011/60103076_0.shtml
         */

        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
