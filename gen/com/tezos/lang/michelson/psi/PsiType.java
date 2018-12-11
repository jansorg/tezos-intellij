// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.tezos.client.stack.MichelsonStackType;
import com.tezos.lang.michelson.lang.type.TypeMetadata;

public interface PsiType extends PsiAnnotated {

  boolean isComparable();

  @Nullable
  PsiType findParentType();

  boolean hasParentType();

  @Nullable
  TypeMetadata getTypeMetadata();

  @NotNull
  String getTypeNameString();

  @NotNull
  MichelsonStackType asStackType();

}
