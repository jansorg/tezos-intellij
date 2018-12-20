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

public class PsiComplexTypeImpl extends PsiTypeImpl implements PsiComplexType {

  public PsiComplexTypeImpl(@NotNull IElementType type) {
    super(type);
  }

  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitComplexType(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiAnnotationList getAnnotationList() {
    return PsiTreeUtil.getChildOfType(this, PsiAnnotationList.class);
  }

  @Override
  @Nullable
  public PsiTrailingAnnotationList getTrailingAnnotationList() {
    return PsiTreeUtil.getChildOfType(this, PsiTrailingAnnotationList.class);
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

  @Override
  @NotNull
  public List<PsiType> getTypeArguments() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiType.class);
  }

  @Nullable
  public PsiElement getTypeToken() {
    return MichelsonPsiUtil.getTypeToken(this);
  }

  public boolean hasSimpleTypes() {
    return MichelsonPsiUtil.hasSimpleTypes(this);
  }

  public boolean hasComplexTypes() {
    return MichelsonPsiUtil.hasComplexTypes(this);
  }

}
