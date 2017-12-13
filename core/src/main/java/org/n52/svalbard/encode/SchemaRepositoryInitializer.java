package org.n52.svalbard.encode;

import javax.inject.Inject;

import org.n52.janmayen.lifecycle.Constructable;

/**
 * Class to initialize the {@link SchemaRepository}.
 *
 * @author Christian Autermann
 */
public class SchemaRepositoryInitializer implements Constructable {
    private EncoderRepository encoderRepository;
    private SchemaRepository schemaRepository;

    @Override
    public void init() {
        this.schemaRepository.init(this.encoderRepository);
    }

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Inject
    public void setSchemaRepository(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

}
