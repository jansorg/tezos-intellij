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

public abstract class PsiSectionImpl extends MichelsonCompositeImpl implements PsiSection {

  public PsiSectionImpl(@NotNull IElementType type) {
    super(type);
  }

  @Override
  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitSection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiSectionType getSectionType() {
    return MichelsonPsiUtil.getSectionType(this);
  }

}
