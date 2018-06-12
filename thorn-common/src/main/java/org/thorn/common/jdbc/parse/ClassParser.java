/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thorn.common.jdbc.parse;

import org.apache.commons.lang3.StringUtils;
import org.thorn.common.jdbc.model.*;
import org.thorn.common.jdbc.type.TypeHandler;
import org.thorn.common.jdbc.type.impl.DefaultTypeHandler;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类属性解析器.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class ClassParser {

    private static final ConcurrentHashMap<Class, MappingModel> MODEL_MAPPING
            = new ConcurrentHashMap<Class, MappingModel>();

    public static MappingModel parse(Class cls) throws IllegalAccessException, InstantiationException {

        MappingModel model = MODEL_MAPPING.get(cls);

        if(model == null) {
            Table table = (Table) cls.getAnnotation(Table.class);

            // set table name

            if(table == null) {
                throw new IllegalArgumentException(cls.getName() + " not has Table annotation");
            }

            model = new MappingModel();
            String tableName = table.value();
            if(StringUtils.isBlank(tableName)) {
                tableName = cls.getSimpleName().toUpperCase();
            }
            model.setTableName(tableName);

            // set primary keys and column
            Set<PrimaryKey> keys = new HashSet<PrimaryKey>();
            Set<String> columns = new HashSet<String>();

            Field[] fields = cls.getDeclaredFields();

            for(Field field : fields) {

                Primary key = field.getAnnotation(Primary.class);

                if(key != null) {
                    String pk = key.value();

                    if(StringUtils.isBlank(pk)) {
                        pk = field.getName();
                    }

                    PrimaryKey pkModel = new PrimaryKey();
                    pkModel.setName(field.getName());
                    pkModel.setPk(pk);
                    pkModel.setPolicy(key.policy());
                    pkModel.setSql(key.sql());

                    Class<? extends TypeHandler> typeCls = key.type();
                    TypeHandler typeHandler = null;
                    if(typeCls.equals(DefaultTypeHandler.class)) {
                        typeHandler = new DefaultTypeHandler(field.getType());
                    } else {
                        typeHandler = typeCls.newInstance();
                    }

                    keys.add(pkModel);
                    model.getMap().put(pk, field.getName());
                    model.getTypeHandlerMap().put(pk, typeHandler);
                    continue;
                }

                Column column = field.getAnnotation(Column.class);

                if(column != null) {
                    String name = column.value();

                    if(StringUtils.isBlank(name)) {
                        name = field.getName();
                    }

                    Class<? extends TypeHandler> typeCls = column.type();
                    TypeHandler typeHandler = null;
                    if(typeCls.equals(DefaultTypeHandler.class)) {
                        typeHandler = new DefaultTypeHandler(field.getType());
                    } else {
                        typeHandler = typeCls.newInstance();
                    }

                    columns.add(name);
                    model.getMap().put(name, field.getName());
                    model.getTypeHandlerMap().put(name, typeHandler);
                    continue;
                }
            }

            model.setColumns(columns.toArray(new String[0]));
            model.setPrimaryKeys(keys.toArray(new PrimaryKey[0]));

            // put model
            MappingModel _model = MODEL_MAPPING.get(cls);
            if(_model == null) {
                MODEL_MAPPING.put(cls, model);
            } else {
                model = _model;
            }
        }

        return model;
    }


}
