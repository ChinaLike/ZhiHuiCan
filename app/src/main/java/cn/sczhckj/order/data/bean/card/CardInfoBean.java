package cn.sczhckj.order.data.bean.card;

import java.util.List;

/**
 * @ describe:  卡片信息
 * @ author: Like on 2016/12/27.
 * @ email: 572919350@qq.com
 */

public class CardInfoBean {

    private String url;//总体介绍

    private List<Card> cards;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public class Card {
        private Integer id;//ID
        private Integer cardId;//卡片类型
        private String name;//卡片名称
        private Double price;//现价
        private Double originPrice;//原价
        private Integer isLock;//是否解锁当前卡片
        private String url;//卡种描述
        private boolean isSelect;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCardId() {
            return cardId;
        }

        public void setCardId(Integer cardId) {
            this.cardId = cardId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getOriginPrice() {
            return originPrice;
        }

        public void setOriginPrice(Double originPrice) {
            this.originPrice = originPrice;
        }

        public Integer getIsLock() {
            return isLock;
        }

        public void setIsLock(Integer isLock) {
            this.isLock = isLock;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", cardId=" + cardId +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", originPrice=" + originPrice +
                    ", isLock=" + isLock +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", cards=" + cards +
                '}';
    }
}
