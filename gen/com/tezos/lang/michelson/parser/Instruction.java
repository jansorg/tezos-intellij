// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface Instruction extends PsiElement {

  @Nullable
  ComparableType getComparableType();

  @Nullable
  DataToplevel getDataToplevel();

  @Nullable
  Instructions getInstructions();

  @Nullable
  Type getType();

  @Nullable
  TypeToplevel getTypeToplevel();

}
