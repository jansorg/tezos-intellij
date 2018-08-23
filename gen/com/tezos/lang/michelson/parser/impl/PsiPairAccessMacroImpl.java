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

public class PsiPairAccessMacroImpl extends PsiMacroInstructionImpl implements PsiPairAccessMacro {

  public PsiPairAccessMacroImpl(@NotNull IElementType type) {
    super(type);
  }

  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitPairAccessMacro(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiBlockInstruction getBlockInstruction() {
    return PsiTreeUtil.getChildOfType(this, PsiBlockInstruction.class);
  }

  @Override
  @Nullable
  public PsiElement getMacroDiipToken() {
    return findPsiChildByType(MACRO_DIIP_TOKEN);
  }

  @Override
  @Nullable
  public PsiElement getMacroDuupToken() {
    return findPsiChildByType(MACRO_DUUP_TOKEN);
  }

  @Override
  @Nullable
  public PsiElement getMacroNestedToken() {
    return findPsiChildByType(MACRO_NESTED_TOKEN);
  }

  @Override
  @Nullable
  public PsiElement getMacroPairsToken() {
    return findPsiChildByType(MACRO_PAIRS_TOKEN);
  }

  @Override
  @Nullable
  public PsiElement getMacroPairAccessToken() {
    return findPsiChildByType(MACRO_PAIR_ACCESS_TOKEN);
  }

}
