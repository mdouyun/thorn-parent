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

package org.thorn.common.io;

import java.io.Serializable;

/**
 * TODO.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class LittleFriend extends Friend implements Serializable {

    private Long height;

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public LittleFriend(String name, int age) {
        super(name, age);
    }

    public LittleFriend(String name, int age, Long height) {
        super(name, age);
        this.height = height;
    }



    @Override
    public String toString() {
        return "LittleFriend{" +
                "height=" + height +
                "} " + super.toString();
    }
}
