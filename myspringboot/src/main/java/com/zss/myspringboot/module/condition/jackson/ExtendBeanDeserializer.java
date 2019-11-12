package com.zss.myspringboot.module.condition.jackson;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.BeanDeserializer;
import org.codehaus.jackson.map.deser.SettableBeanProperty;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class ExtendBeanDeserializer extends BeanDeserializer {
    public ExtendBeanDeserializer(BeanDeserializer src) {
        super(src, true);
    }

    public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._nonStandardCreation) {
            if (this._unwrappedPropertyHandler != null) {
                return this.deserializeWithUnwrapped(jp, ctxt);
            } else {
                return this._externalTypeIdHandler != null ? this.deserializeWithExternalTypeId(jp, ctxt) : this.deserializeFromObjectUsingNonDefault(jp, ctxt);
            }
        } else {
            Object bean = this._valueInstantiator.createUsingDefault();
            if (this._injectables != null) {
                this.injectValues(ctxt, bean);
            }

            for(; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
                String propName = jp.getCurrentName();
                jp.nextToken();
                if (propName.indexOf(".") > -1) {
                    String value = jp.getText();
                    String[] props = propName.split("\\.");

                    try {
                        String parent = "";

                        for(int i = 0; i < props.length; ++i) {
                            String prop = parent == "" ? props[i] : parent + "." + props[i];
                            if (i < props.length - 1) {
                                Object o = PropertyUtils.getProperty(bean, prop);
                                if (o == null) {
                                    o = PropertyUtils.getPropertyType(bean, prop).newInstance();
                                    PropertyUtils.setProperty(bean, prop, o);
                                }
                            } else {
                                Class<?> type = PropertyUtils.getPropertyType(bean, prop);
                                if ((!StringUtils.hasText(value) || "null".equals(value)) && !type.isPrimitive()) {
                                    PropertyUtils.setProperty(bean, prop, (Object)null);
                                } else {
                                    PropertyUtils.setProperty(bean, prop, ConvertUtils.convert(value, type));
                                }
                            }

                            parent = prop;
                        }
                    } catch (Exception var12) {
                        this.wrapAndThrow(var12, bean, propName, ctxt);
                    }
                } else {
                    SettableBeanProperty prop = this._beanProperties.find(propName);
                    if (prop != null) {
                        try {
                            prop.deserializeAndSet(jp, ctxt, bean);
                        } catch (Exception var11) {
                            this.wrapAndThrow(var11, bean, propName, ctxt);
                        }
                    } else {
                        this._handleUnknown(jp, ctxt, bean, propName);
                    }
                }
            }

            return bean;
        }
    }

    private final void _handleUnknown(JsonParser jp, DeserializationContext ctxt, Object bean, String propName) throws IOException, JsonProcessingException {
        if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
            jp.skipChildren();
        } else if (this._anySetter != null) {
            try {
                this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
            } catch (Exception var6) {
                this.wrapAndThrow(var6, bean, propName, ctxt);
            }
        } else {
            this.handleUnknownProperty(jp, ctxt, bean, propName);
        }

    }
}
