// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.tezos.lang.michelson.psi.MichelsonComposite;

public interface PsiType extends MichelsonComposite {

  @NotNull
  List<PsiType> getTypeList();

  @NotNull
  List<PsiAnnotation> getAnnotations();

}
