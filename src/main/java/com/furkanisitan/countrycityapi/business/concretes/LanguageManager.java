package com.furkanisitan.countrycityapi.business.concretes;

import com.furkanisitan.countrycityapi.business.LanguageService;
import com.furkanisitan.countrycityapi.business.mappers.LanguageMapper;
import com.furkanisitan.countrycityapi.business.validators.LanguageValidator;
import com.furkanisitan.countrycityapi.dataaccess.LanguageRepository;
import com.furkanisitan.countrycityapi.model.entities.Language;
import com.furkanisitan.countrycityapi.model.requests.LanguageCreateRequest;
import com.furkanisitan.countrycityapi.model.requests.LanguageUpdateRequest;
import com.furkanisitan.countrycityapi.model.responses.LanguageResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@Primary
public class LanguageManager implements LanguageService {

    private final LanguageRepository repository;
    private final LanguageValidator validator;

    public LanguageManager(LanguageRepository repository, LanguageValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public List<LanguageResponse> findAll() {
        return LanguageMapper.INSTANCE.toResponseList(repository.findAll());
    }

    @Override
    public LanguageResponse findById(Long id) {
        return LanguageMapper.INSTANCE.toResponse(repository.findById(id).orElse(null));
    }

    @Transactional
    @Override
    public LanguageResponse create(LanguageCreateRequest request) {

        validator.codeIsUnique(request.getCode());

        Language language = LanguageMapper.INSTANCE.fromCreateRequest(request);

        return LanguageMapper.INSTANCE.toResponse(repository.save(language));
    }

    @Transactional
    @Override
    public void update(LanguageUpdateRequest request) {

        Language language = validator.findIfIdIsExists(request.getId());
        validator.codeIsUnique(request.getCode(), request.getId());

        LanguageMapper.INSTANCE.updateFromUpdateRequest(request, language);

        repository.save(language);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        validator.idIsExists(id);
        repository.deleteById(id);
    }
}