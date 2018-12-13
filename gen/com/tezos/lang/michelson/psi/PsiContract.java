// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiContract extends MichelsonComposite {

  @NotNull
  List<PsiSection> getSections();

  boolean isMainContract();

  @Nullable
  PsiSection findSectionByType(@NotNull PsiSectionType type);

  @Nullable
  PsiTypeSection findParameterSection();

  @Nullable
  PsiTypeSection findStorageSection();

  @Nullable
  PsiCodeSection findCodeSection();

}
