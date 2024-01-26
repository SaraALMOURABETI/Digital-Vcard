package com.example.DigitalCard.controller;

import com.example.DigitalCard.entity.InscriptionForm;
import com.example.DigitalCard.repository.FormRepository;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/inscription-forms")
public class InscriptionFormController {

    @Autowired
    private FormRepository formRepository;
    private Long fixedId = null;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("inscriptionForm", new InscriptionForm());
        return "formulaire";
    }

    @PostMapping("/create")
    public String createForm(@ModelAttribute InscriptionForm inscriptionForm) {
        formRepository.save(inscriptionForm);
        return "redirect:/inscription-forms/dashboard";
    }


    @GetMapping("/generateQRCode/{id}")
    public String generateQRCode(@PathVariable("id") Long id, Model model) throws IOException, WriterException {
        InscriptionForm inscriptionForm = formRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inscription form ID: " + id));

        String ipAddress = getWifiIPv4Address();
        int port = 8074;
        String qrCodeText = "http://" + ipAddress + ":" + port + "/inscription-forms/scan/" + inscriptionForm.getId();

        String base64Image = generateQRCodeImage(qrCodeText);
        model.addAttribute("qrCodeImage", base64Image);
        model.addAttribute("inscriptionForm", inscriptionForm);

        return "qr_code";
    }

    private InscriptionForm saveInscriptionForm(InscriptionForm inscriptionForm) {
        return formRepository.save(inscriptionForm);
    }

    private String getWifiIPv4Address() throws IOException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (networkInterface.getName().startsWith("wlan")) {
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        throw new RuntimeException("Unable to retrieve Wi-Fi IPv4 address.");
    }

    @GetMapping("/page")
    public String showpage(Model model) {
        List<InscriptionForm> forms = formRepository.findAll();
        model.addAttribute("forms", forms);
        return "page";
    }


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<InscriptionForm> forms = formRepository.findAll();
        model.addAttribute("forms", forms);
        return "dashboard";
    }

    @GetMapping("/scan/{id}")
    public String scanForm(@PathVariable("id") Long id, Model model) {
        InscriptionForm inscriptionForm = formRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inscription form ID: " + id));
        model.addAttribute("inscriptionForm", inscriptionForm);
        return "profil";
    }

    private String generateQRCodeImage(String qrCodeText) throws WriterException, IOException {
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height);

        BufferedImage qrCodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", stream);
        byte[] qrCodeBytes = stream.toByteArray();

        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadProfileAsVcf(@PathVariable("id") Long id) throws IOException {
        InscriptionForm inscriptionForm = formRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inscription form ID: " + id));

        String vcfContent = generateVcfContent(inscriptionForm);

        ByteArrayResource resource = new ByteArrayResource(vcfContent.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/vcard"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("profil.vcf").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }


    private String generateVcfContent(InscriptionForm inscriptionForm) {
        StringBuilder vcfBuilder = new StringBuilder();

        vcfBuilder.append("BEGIN:VCARD").append("\n");
        vcfBuilder.append("VERSION:3.0").append("\n");
        vcfBuilder.append("N:").append(inscriptionForm.getNom()).append(";").append(inscriptionForm.getPrenom()).append("\n");
        //  vcfBuilder.append("FN:").append(inscriptionForm.getPrenom()).append(" ").append(inscriptionForm.getNom()).append("\n");

        vcfBuilder.append("TEL;TYPE=HOME,VOICE:").append(inscriptionForm.getNumero()).append("\n");
        vcfBuilder.append("EMAIL;TYPE=INTERNET:").append(inscriptionForm.getEmail()).append("\n");
        vcfBuilder.append("ORG:").append(inscriptionForm.getNomSociete()).append("\n");
        vcfBuilder.append("TITLE:").append(inscriptionForm.getPoste()).append("\n");
        vcfBuilder.append("NOTE:").append(inscriptionForm.getAbout()).append("\n");
        vcfBuilder.append("URL:").append(inscriptionForm.getSiteweb()).append("\n");
        // vcfBuilder.append("X-INSTAGRAM:").append(inscriptionForm.getInstagram()).append("\n");
        /*String instagram = inscriptionForm.getInstagram();
        if (instagram != null && !instagram.isEmpty()) {
            vcfBuilder.append("X-INSTAGRAM:").append(instagram).append("\n");
        }*/
     /*   vcfBuilder.append("X-LINKEDIN:").append(inscriptionForm.getLinkedIn()).append("\n");
        vcfBuilder.append("X-FACEBOOK:").append(inscriptionForm.getFacebookCompany()).append("\n");
        vcfBuilder.append("X-LINKEDIN-COMPANY:").append(inscriptionForm.getLinkedInCompany()).append("\n");
        vcfBuilder.append("X-INSTAGRAM-COMPANY:").append(inscriptionForm.getInstaCompany()).append("\n");*/
        //vcfBuilder.append("GENRE:").append(inscriptionForm.getGenre()).append("\n");
        vcfBuilder.append("END:VCARD").append("\n");
        return vcfBuilder.toString();
    }



    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        InscriptionForm inscriptionForm;

        if (id.equals(fixedId)) {

            inscriptionForm = formRepository.findById(fixedId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid inscription form ID: " + fixedId));
        } else {
            inscriptionForm = formRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid inscription form ID: " + id));
        }

        model.addAttribute("inscriptionForm", inscriptionForm);
        return "formulaire";
    }

    @GetMapping("/{id}/delete")
        public String deleteInscriptionForm(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
            InscriptionForm inscriptionForm = formRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid inscription form ID: " + id));

            formRepository.delete(inscriptionForm);
            redirectAttributes.addFlashAttribute("successMessage", "Inscription form deleted successfully.");
            return "redirect:/inscription-forms/dashboard";
        }

}
