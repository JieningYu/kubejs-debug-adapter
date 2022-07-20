package com.warmthdawn.kubejsdebugadapter.mixin;

import com.warmthdawn.kubejsdebugadapter.api.DebuggableScript;
import com.warmthdawn.kubejsdebugadapter.api.Debugger;
import com.warmthdawn.kubejsdebugadapter.api.IDebuggableContext;
import com.warmthdawn.kubejsdebugadapter.api.IDebuggableScriptProvider;
import dev.latvian.mods.rhino.*;
import dev.latvian.mods.rhino.ast.AstRoot;
import dev.latvian.mods.rhino.ast.ScriptNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = Context.class, remap = false)
public abstract class MixinContext implements IDebuggableContext {

    @Shadow
    private boolean sealed;

    @Shadow
    static void onSealedMutation() {
    }

    private Debugger debugger;
    private Object debuggerData;


    @Override
    public final Debugger getDebugger() {
        return debugger;
    }

    @Override
    public final Object getDebuggerContextData() {
        return debuggerData;
    }

    @Override
    public final void setDebugger(Debugger debugger, Object contextData) {
        if (sealed) onSealedMutation();
        this.debugger = debugger;
        debuggerData = contextData;
    }


    @Inject(method = "parse", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject_parse(String sourceString, String sourceName, int lineno, CompilerEnvirons compilerEnv, ErrorReporter compilationErrorReporter, boolean returnFunction, CallbackInfoReturnable<ScriptNode> cir, Parser p, AstRoot ast) {

    }


}
