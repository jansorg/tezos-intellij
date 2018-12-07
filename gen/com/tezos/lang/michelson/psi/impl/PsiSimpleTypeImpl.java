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

public class PsiSimpleTypeImpl extends PsiTypeImpl implements PsiSimpleType {

  public PsiSimpleTypeImpl(@NotNull IElementType type) {
    super(type);
  }

  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitSimpleType(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getTypeName() {
    return findPsiChildByType(TYPE_NAME);
  }

  @Override
  @Nullable
  public PsiElement getTypeNameComparable() {
    return findPsiChildByType(TYPE_NAME_COMPARABLE);
  }

  @NotNull
  public PsiElement getTypeToken() {
    return MichelsonPsiUtil.getTypeToken(this);
  }

}
