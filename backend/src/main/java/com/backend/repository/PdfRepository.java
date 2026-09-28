package com.backend.repository;

import com.backend.entity.Pdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfRepository extends JpaRepository<Pdf, Long> {

    /**
     * Find a pdf by fail id.
     *
     * @param id - id of fail of the searched pdf
     * @return pdf with the given fail id
     */
    Pdf findPdfByFailId(Long id);

    /**
     * Delete a pdf by fail id.
     *
     * @param id - id of fail of the searched pdf
     */
    void deletePdfByFailId(Long id);

}