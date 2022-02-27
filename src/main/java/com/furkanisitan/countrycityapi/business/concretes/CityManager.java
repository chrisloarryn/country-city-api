package com.furkanisitan.countrycityapi.business.concretes;

import com.furkanisitan.countrycityapi.business.CityService;
import com.furkanisitan.countrycityapi.business.mappers.CityMapper;
import com.furkanisitan.countrycityapi.business.validators.CityValidator;
import com.furkanisitan.countrycityapi.business.validators.CountryValidator;
import com.furkanisitan.countrycityapi.dataaccess.CityRepository;
import com.furkanisitan.countrycityapi.model.entities.City;
import com.furkanisitan.countrycityapi.model.entities.Country;
import com.furkanisitan.countrycityapi.model.requests.CityCreateRequest;
import com.furkanisitan.countrycityapi.model.requests.CityUpdateRequest;
import com.furkanisitan.countrycityapi.model.responses.CityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@Primary
public class CityManager implements CityService {

    private final CityRepository repository;
    private final CityValidator validator;
    private final CountryValidator countryValidator;

    @Autowired
    public CityManager(CityRepository repository, CityValidator validator, CountryValidator countryValidator) {
        this.repository = repository;
        this.validator = validator;
        this.countryValidator = countryValidator;
    }

    @Override
    public List<CityResponse> findAll() {
        return CityMapper.INSTANCE.toResponseList(repository.findAll());
    }

    @Override
    public CityResponse findById(Long id) {
        return CityMapper.INSTANCE.toResponse(repository.findById(id).orElse(null));
    }

    @Transactional
    @Override
    public CityResponse create(CityCreateRequest request) {

        Country country = countryValidator.getIfCodeForeignKeyIsExists(request.getCountryCode());

        City city = CityMapper.INSTANCE.fromCreateRequest(request);
        city.setCountry(country);

        return CityMapper.INSTANCE.toResponse(repository.save(city));
    }

    @Transactional
    @Override
    public void update(CityUpdateRequest request) {

        City city = validator.findIfIdIsExists(request.getId());
        Country country = countryValidator.getIfCodeForeignKeyIsExists(request.getCountryCode());

        CityMapper.INSTANCE.updateFromUpdateRequest(request, city);
        city.setCountry(country);

        repository.save(city);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {

        validator.idIsExists(id);
        repository.deleteById(id);
    }
}
