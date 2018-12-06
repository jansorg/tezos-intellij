// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.tezos.lang.michelson.psi.*;
import com.tezos.lang.michelson.lang.AnnotationType;
import com.intellij.psi.tree.IElementType;

public abstract class PsiAnnotationImpl extends MichelsonCompositeImpl implements PsiAnnotation {

  public PsiAnnotationImpl(@NotNull IElementType type) {
    super(type);
  }

  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitAnnotation(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  public boolean isTypeAnnotation() {
    return MichelsonPsiUtil.isTypeAnnotation(this);
  }

  public boolean isVariableAnnotation() {
    return MichelsonPsiUtil.isVariableAnnotation(this);
  }

  public boolean isFieldAnnotation() {
    return MichelsonPsiUtil.isFieldAnnotation(this);
  }

  @NotNull
  public AnnotationType getAnnotationType() {
    return MichelsonPsiUtil.getAnnotationType(this);
  }

  @Nullable
  public PsiInstruction findParentInstruction() {
    return MichelsonPsiUtil.findParentInstruction(this);
  }

  @Nullable
  public PsiType findParentType() {
    return MichelsonPsiUtil.findParentType(this);
  }

  @Nullable
  public PsiData findParentData() {
    return MichelsonPsiUtil.findParentData(this);
  }

}
