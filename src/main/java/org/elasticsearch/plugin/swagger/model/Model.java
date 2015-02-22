package org.elasticsearch.plugin.swagger.model;

import java.util.List;
import java.util.Map;

/**
 * A Model Object holds the definition of a new model for this API Declaration.
 */
public class Model extends SwaggerModel {
    /**
     * A unique identifier for the model.
     */
    private String id;

    /**
     * A brief description of this model.
     */
    private String descriptionø;

    /**
     * A definition of which properties MUST exist when a model instance is produced.
     */
    private List<String> required;

    /**
     * A list of properties (fields) that are part of the model.
     */
    private Map<String, ModelProperty> properties;

    /**
     * List of the model ids that inherit from this model. Sub models inherit all the properties of
     * the parent model. Since inheritance is transitive, if the parent of a model inherits from
     * another model, its sub-model will include all properties. As such, if you have Foo->Bar->Baz,
     * then Baz will inherit the properties of Bar and Foo.
     */
    private List<String> subTypes;

    /**
     * This field allows for polymorphism within the described inherited models.
     */
    private String discriminator;
}