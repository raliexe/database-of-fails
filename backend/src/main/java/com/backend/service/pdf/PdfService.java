package com.backend.service.pdf;

import com.backend.entity.Fail;
import com.backend.entity.Pdf;

public interface PdfService {

    /**
     * Finds pdf by given fail id.
     *
     * @param id - id of fail of the searched pdf
     * @return pdf with the given fail id
     */
    byte[] findPdfByFailId(Long id);

    /**
     * Creates pdf for fail with given id.
     *
     * @param fail - id of the fail
     */
    Pdf createPdfForFail(Fail fail);

}