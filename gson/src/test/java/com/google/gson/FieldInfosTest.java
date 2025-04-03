/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldAttributes} class.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public class FieldInfosTest {
  private FieldInfos fieldInfos;

  @Before
  public void setUp() throws Exception {
    fieldInfos = new FieldInfos(Foo.class.getField("bar"));
  }

  @Test
  public void testNullField() {
    assertThrows(NullPointerException.class, () -> new FieldInfos(null));
  }

  @Test
  public void testDeclaringClass() {
    assertThat(fieldInfos.getDeclaringClass()).isAssignableTo(Foo.class);
  }

  @Test
  public void testModifiers() {
    assertThat(fieldInfos.hasModifier(Modifier.STATIC)).isFalse();
    assertThat(fieldInfos.hasModifier(Modifier.FINAL)).isFalse();
    assertThat(fieldInfos.hasModifier(Modifier.ABSTRACT)).isFalse();
    assertThat(fieldInfos.hasModifier(Modifier.VOLATILE)).isFalse();
    assertThat(fieldInfos.hasModifier(Modifier.PROTECTED)).isFalse();

    assertThat(fieldInfos.hasModifier(Modifier.PUBLIC)).isTrue();
    assertThat(fieldInfos.hasModifier(Modifier.TRANSIENT)).isTrue();
  }

  @Test
public void testHasModifier() throws NoSuchFieldException {
    // Création d'une classe Foo avec un champ public
    class Foo {
        public String bar;
    }

    // Créer un FieldInfos pour le champ "bar"
    FieldInfos fieldInfo = new FieldInfos(Foo.class.getField("bar"));

    // Test pour vérifier si le champ "bar" est public
    assertThat(fieldInfo.hasModifier(Modifier.PUBLIC)).isTrue();
    
    // Test pour vérifier si le champ "bar" n'est pas static
    assertThat(fieldInfo.hasModifier(Modifier.STATIC)).isFalse();
}

  @Test
  public void testName() {
    assertThat(fieldInfos.getName()).isEqualTo("bar");
  }

  @Test
  public void testDeclaredTypeAndClass() {
    Type expectedType = new TypeToken<List<String>>() {}.getType();
    assertThat(fieldInfos.getDeclaredType()).isEqualTo(expectedType);
    assertThat(fieldInfos.getDeclaredClass()).isAssignableTo(List.class);
  }

  private static class Foo {
    @SuppressWarnings("unused")
    public transient List<String> bar;
  }
}
