// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tezos.lang.michelson.MichelsonTypes.*;
import com.tezos.lang.michelson.psi.impl.MichelsonCompositeImpl;
import com.tezos.lang.michelson.parser.*;
import com.intellij.psi.tree.IElementType;

public class PsiInstructionImpl extends MichelsonCompositeImpl implements PsiInstruction {

  public PsiInstructionImpl(@NotNull IElementType type) {
    super(type);
  }

  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitInstruction(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiData getData() {
    return PsiTreeUtil.getChildOfType(this, PsiData.class);
  }

  @Override
  @Nullable
  public PsiInstructions getInstructions() {
    return PsiTreeUtil.getChildOfType(this, PsiInstructions.class);
  }

  @Override
  @Nullable
  public PsiType getType() {
    return PsiTreeUtil.getChildOfType(this, PsiType.class);
  }

  @Override
  @Nullable
  public PsiElement getInt() {
    return findPsiChildByType(INT);
  }

  @Override
  @Nullable
  public PsiElement getString() {
    return findPsiChildByType(STRING);
  }

}
