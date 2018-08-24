// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tezos.lang.michelson.MichelsonTypes.*;
import com.tezos.lang.michelson.parser.*;
import com.intellij.psi.tree.IElementType;

public class PsiConditionalInstructionImpl extends PsiInstructionImpl implements PsiConditionalInstruction {

  public PsiConditionalInstructionImpl(@NotNull IElementType type) {
    super(type);
  }

  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitConditionalInstruction(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiBlockInstruction> getBlockInstructionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiBlockInstruction.class);
  }

  @Override
  @NotNull
  public PsiBlockInstruction getTrueBranch() {
    List<PsiBlockInstruction> p1 = getBlockInstructionList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public PsiBlockInstruction getFalseBranch() {
    List<PsiBlockInstruction> p1 = getBlockInstructionList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}
