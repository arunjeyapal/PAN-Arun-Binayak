/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreemnets.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package opennlp.tools.coref;

/**
 * Enumeration of modes in which a linker can run.
 */
public enum LinkerMode {

  /**
   * Testing mode, used to identify coreference relationships in un-annotated text.
   */
  TEST,

  /**
   * Training mode, used to learn coreference relationships in annotated text.
   */
  TRAIN,

  /** Evaluation mode, used to evaluate identifed coreference relationships based on annotated text. */
  EVAL,

  /**
   * Training mode, used to learn coreference relationships in annotated text.
   */
  SIM
}
