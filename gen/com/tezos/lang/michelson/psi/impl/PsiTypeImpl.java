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
import com.tezos.client.stack.MichelsonStackType;
import com.tezos.lang.michelson.lang.type.TypeMetadata;
import com.intellij.psi.tree.IElementType;

public abstract class PsiTypeImpl extends MichelsonCompositeImpl implements PsiType {

  public PsiTypeImpl(@NotNull IElementType type) {
    super(type);
  }

  @Override
  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public boolean isComparable() {
    return MichelsonPsiUtil.isComparable(this);
  }

  @Override
  @Nullable
  public PsiType findParentType() {
    return MichelsonPsiUtil.findParentType(this);
  }

  @Override
  public boolean hasParentType() {
    return MichelsonPsiUtil.hasParentType(this);
  }

  @Override
  @Nullable
  public TypeMetadata getTypeMetadata() {
    return MichelsonPsiUtil.getTypeMetadata(this);
  }

  @Override
  @NotNull
  public String getTypeNameString() {
    return MichelsonPsiUtil.getTypeNameString(this);
  }

  @Override
  @NotNull
  public MichelsonStackType asStackType() {
    return MichelsonPsiUtil.asStackType(this);
  }

  @Override
  @Nullable
  public PsiElement getTypeToken() {
    return MichelsonPsiUtil.getTypeToken(this);
  }

}
