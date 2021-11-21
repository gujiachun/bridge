package com.rainbow.bridge.targetcore.handler;

/**
 * @author gujiachun
 */
public abstract class AbsEntryHandlerFactory implements EntryHandlerFactory {

    protected String targetType;

    public AbsEntryHandlerFactory(String targetType){
        this.targetType = targetType;
    }

    @Override
    public boolean support(String targetType) {
        if (this.targetType.equals(targetType)){
            return true;
        }
        return false;
    }
}
