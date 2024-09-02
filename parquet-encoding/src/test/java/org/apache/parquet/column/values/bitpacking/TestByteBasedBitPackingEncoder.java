/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.parquet.column.values.bitpacking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.parquet.bytes.BytesUtils;
import org.junit.jupiter.api.Test;

public class TestByteBasedBitPackingEncoder {

  @Test
  public void testSlabBoundary() {
    for (int i = 0; i <= 32; i++) {
      final ByteBasedBitPackingEncoder encoder = new ByteBasedBitPackingEncoder(i, Packer.BIG_ENDIAN);
      // make sure to write through the progression of slabs
      final int totalValues = 191 * 1024 * 8 + 10;
      for (int j = 0; j < totalValues; j++) {
        try {
          encoder.writeInt(j);
        } catch (Exception e) {
          throw new RuntimeException(i + ": error writing " + j, e);
        }
      }
      assertEquals(BytesUtils.paddedByteCountFromBits(totalValues * i), encoder.getBufferSize());
      assertEquals(i == 0 ? 1 : 9, encoder.getNumSlabs());
    }
  }
}
