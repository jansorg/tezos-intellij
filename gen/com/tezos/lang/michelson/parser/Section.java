// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface Section extends PsiElement {

  @Nullable
  CodeSection getCodeSection();

  @Nullable
  ParameterSection getParameterSection();

  @Nullable
  ReturnSection getReturnSection();

  @Nullable
  StorageSection getStorageSection();

}
