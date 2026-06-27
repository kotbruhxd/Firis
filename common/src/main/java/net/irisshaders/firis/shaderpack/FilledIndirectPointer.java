package net.irisshaders.firis.shaderpack;

import net.irisshaders.firis.gl.buffer.ShaderStorageBufferHolder;
import net.irisshaders.firis.shaderpack.properties.IndirectPointer;

public record FilledIndirectPointer(int buffer, long offset) {
	public static FilledIndirectPointer basedOff(ShaderStorageBufferHolder holder, IndirectPointer pointer) {
		if (pointer == null || holder == null) return null;

		return new FilledIndirectPointer(holder.getBufferIndex(pointer.buffer()), pointer.offset());
	}
}
