package com.example.DigitalCard.entity;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Entity
@Table(name = "inscription_form")
public class InscriptionForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Lob
    @Column(name = "profile_pic")
    private File profilePic;

    public File getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(File profilePic) {
        this.profilePic = profilePic;
    }*/

  /*  public File getBackgroundPic() {
        return backgroundPic;
    }

    public void setBackgroundPic(File backgroundPic) {
        this.backgroundPic = backgroundPic;
    }


*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

  /*  @Lob
    @Column(name = "background_pic")
    private File backgroundPic;*/

    @Column(name = "Nom")
    private String Nom;

    @Column(name = "Prenom")
    private String Prenom;

    @Column(name = "numero")
    private String numero;

    @Column(name = "email")
    private String email;

    @Column(name = "nom_societe")
    private String nomSociete;

    @Column(name = "poste")
    private String poste;

    @Column(name = "resume")
    private String about;

    @Column(name = "siteweb")
    private String siteweb;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "linkedin")
    private String linkedIn;

    @Column(name = "phone")
    private String phone;

    @Column(name = "insta_company")
    private String instaCompany;

    @Column(name = "facebook_company")
    private String facebookCompany;

    @Column(name = "linkedin_company")
    private String linkedInCompany;

    @Column(name = "genre")
    private String genre;



   /* @Lob
    @Column(name = "gallery")
    private List<File> gallery;

    public List<File> getGallery() {
        return gallery;
    }

    public void setGallery(List<File> gallery) {
        this.gallery = gallery;
    }*/

    public InscriptionForm(Long id,String nom, String prenom, String numero, String email, String nomSociete, String poste, String about, String siteweb, String instagram, String linkedIn, String phone, String instaCompany, String facebookCompany, String linkedInCompany, String genre) {
        this.id = id;
        Nom = nom;
        Prenom = prenom;
        this.numero = numero;
        this.email = email;
        this.nomSociete = nomSociete;
        this.poste = poste;
        this.about = about;
        this.siteweb = siteweb;
        this.instagram = instagram;
        this.linkedIn = linkedIn;
        this.phone = phone;
        this.instaCompany = instaCompany;
        this.facebookCompany = facebookCompany;
        this.linkedInCompany = linkedInCompany;
        this.genre = genre;
    }

    // constructors, getters, setters


    public InscriptionForm() {
    }



    public String getNom() {
        return Nom;
    }
    public String getPrenom() {
        return Prenom;
    }


    public String getNumero() {
        return numero;
    }

    public String getEmail() {
        return email;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public String getPoste() {
        return poste;
    }

    public String getAbout() {
        return about;
    }


    public String getSiteweb() {
        return siteweb;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public String getPhone() {
        return phone;
    }

    public String getInstaCompany() {
        return instaCompany;
    }

    public String getFacebookCompany() {
        return facebookCompany;
    }

    public String getLinkedInCompany() {
        return linkedInCompany;
    }


    public String getGenre() {
        return genre;
    }


    public void setNom(String Nom) {
        this.Nom = Nom;
    }
    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setSiteweb(String siteweb) {
        this.siteweb = siteweb;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setInstaCompany(String instaCompany) {
        this.instaCompany = instaCompany;
    }

    public void setFacebookCompany(String facebookCompany) {
        this.facebookCompany = facebookCompany;
    }

    public void setLinkedInCompany(String linkedInCompany) {
        this.linkedInCompany = linkedInCompany;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }


}