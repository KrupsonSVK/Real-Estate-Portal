package model;

public class Inzerat {

    private Integer id;
    private String nazov;
    private Integer cena;
    private String info;
    private String adress;
    private Integer price;
    private String contact;
    private String keywords;
    private String autor;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return nazov;
    }

    public void setName(String name) {
        this.nazov = name;
    }

    public Integer getCena() {
        return cena;
    }

    public void setCena(Integer cena) {
        this.cena = cena;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Inzerat() {
    }

    public Inzerat(String name, Integer id, String info, int price, String city, String contact, String keywords, String autor) {
        setName(name);
        setId(id);
        setInfo(info);
        setPrice(price);
        setAdress(city);
        setContact(contact);
        setKeywords(keywords);
        setAutor(autor);
    }

    public Inzerat(String name, int id, int price,  String kontakt) {
        setName(name);
        setId(id);
        setCena(price);
        setContact(kontakt);
    }

    public Inzerat(String name,String mesto, int id, int price, String info) {
        setName(name);
        setId(id);
        setCena(price);
        setAdress(mesto);
        setInfo(info);
    }


}

