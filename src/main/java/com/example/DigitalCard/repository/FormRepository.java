package com.example.DigitalCard.repository;

import com.example.DigitalCard.entity.InscriptionForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<InscriptionForm, Long> {

}
