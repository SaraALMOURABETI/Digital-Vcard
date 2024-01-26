package com.example.DigitalCard.service.impl;


import com.example.DigitalCard.entity.InscriptionForm;
import com.example.DigitalCard.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.List;

@Service
public class InscriptionFormService {

    private FormRepository FormRepository;
    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    public InscriptionFormService(FormRepository inscriptionFormRepository) {
        this.FormRepository = inscriptionFormRepository;
    }

    public InscriptionForm save(InscriptionForm inscriptionForm) {
        return FormRepository.save(inscriptionForm);
    }



    private String saveImage(MultipartFile image) throws Exception {
        if (image != null && !image.isEmpty()) {
            // Implement the logic to save the image to a specific location or storage service
            // Return the URL or path of the saved image file
            // Example code:
            // String savedImagePath = storageService.saveImage(image);
            // return savedImagePath;
        }
        return null;
    }


        public List<InscriptionForm> listAll() {
            return FormRepository.findAll(Sort.by("email").ascending());
        }



    // Define any other service methods here, if needed
}
