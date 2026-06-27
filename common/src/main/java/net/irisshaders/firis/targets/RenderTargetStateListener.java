package net.irisshaders.firis.targets;

public interface RenderTargetStateListener {
	RenderTargetStateListener NOP = bound -> {

	};

	void setIsMainBound(boolean bound);
}
