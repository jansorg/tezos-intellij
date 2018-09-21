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

public class PsiContractWrapperImpl extends MichelsonCompositeImpl implements PsiContractWrapper {

  public PsiContractWrapperImpl(@NotNull IElementType type) {
    super(type);
  }

  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitContractWrapper(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiContract getContract() {
    return PsiTreeUtil.getChildOfType(this, PsiContract.class);
  }

}