// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tezos.lang.michelson.MichelsonTypes.*;
import com.tezos.lang.michelson.psi.*;
import com.intellij.psi.tree.IElementType;

public class PsiContractImpl extends MichelsonCompositeImpl implements PsiContract {

  public PsiContractImpl(@NotNull IElementType type) {
    super(type);
  }

  @Override
  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitContract(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public boolean isMainContract() {
    return MichelsonPsiUtil.isMainContract(this);
  }

  @Override
  @NotNull
  public List<PsiSection> getSections() {
    return MichelsonPsiUtil.getSections(this);
  }

  @Override
  @Nullable
  public PsiSection findSectionByType(@NotNull PsiSectionType type) {
    return MichelsonPsiUtil.findSectionByType(this, type);
  }

  @Override
  @Nullable
  public PsiTypeSection findParameterSection() {
    return MichelsonPsiUtil.findParameterSection(this);
  }

  @Override
  @Nullable
  public PsiTypeSection findStorageSection() {
    return MichelsonPsiUtil.findStorageSection(this);
  }

  @Override
  @Nullable
  public PsiCodeSection findCodeSection() {
    return MichelsonPsiUtil.findCodeSection(this);
  }

}
