package model;


public class Statistic {

    private Integer total_ads;
    private Integer total_users;
    private String favorite_ad;
    private Integer ad_liked;
    private String best_user;
    private Integer ads_created;


    public Statistic(int total_ads, int total_users, String favorite_ad, int ad_liked, String best_user, int ads_created) {
        setTotal_ads(total_ads);
        setTotal_users(total_users);
        setFavorite_ad(favorite_ad);
        setAd_liked(ad_liked);
        setBest_user(best_user);
        setAds_created(ads_created);
    }

    public Integer getTotal_ads() {
        return total_ads;
    }

    public void setTotal_ads(Integer total_ads) {
        this.total_ads = total_ads;
    }

    public Integer getTotal_users() {
        return total_users;
    }

    public void setTotal_users(Integer total_users) {
        this.total_users = total_users;
    }

    public String getFavorite_ad() {
        return favorite_ad;
    }

    public void setFavorite_ad(String favorite_ad) {
        this.favorite_ad = favorite_ad;
    }

    public Integer getAd_liked() {
        return ad_liked;
    }

    public void setAd_liked(Integer ad_liked) {
        this.ad_liked = ad_liked;
    }

    public String getBest_user() {
        return best_user;
    }

    public void setBest_user(String best_user) {
        this.best_user = best_user;
    }

    public Integer getAds_created() {
        return ads_created;
    }

    public void setAds_created(Integer ads_created) {
        this.ads_created = ads_created;
    }
}
