package net.irisshaders.firis.mixin.vertices;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferVertexConsumer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.irisshaders.firis.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.firis.uniforms.CapturedRenderingState;
import net.irisshaders.firis.vertices.BlockSensitiveBufferBuilder;
import net.irisshaders.firis.vertices.BufferBuilderPolygonView;
import net.irisshaders.firis.vertices.ExtendedDataHelper;
import net.irisshaders.firis.vertices.ExtendingBufferBuilder;
import net.irisshaders.firis.vertices.FirisExtendedBufferBuilder;
import net.irisshaders.firis.vertices.FirisVertexFormats;
import net.irisshaders.firis.vertices.NormI8;
import net.irisshaders.firis.vertices.NormalHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

/**
 * Dynamically and transparently extends the vanilla vertex formats with additional data
 */
@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder extends DefaultedVertexConsumer implements BufferVertexConsumer, BlockSensitiveBufferBuilder, ExtendingBufferBuilder, FirisExtendedBufferBuilder {
	@Unique
	private final BufferBuilderPolygonView polygon = new BufferBuilderPolygonView();
	@Unique
	private final Vector3f normal = new Vector3f();
	@Unique
	private boolean firis$shouldNotExtend;
	@Unique
	private boolean extending;
	@Unique
	private boolean firis$isTerrain;
	@Unique
	private boolean injectNormalAndUV1;
	@Unique
	private int firis$vertexCount;
	@Unique
	private short currentBlock = -1;
	@Unique
	private short currentRenderType = -1;
	@Unique
	private int currentLocalPosX;
	@Unique
	private int currentLocalPosY;
	@Unique
	private int currentLocalPosZ;
	@Shadow
	private ByteBuffer buffer;

	@Shadow
	private VertexFormat.Mode mode;

	@Shadow
	private VertexFormat format;

	@Shadow
	private int nextElementByte;

	@Shadow
	private @Nullable VertexFormatElement currentElement;

	@Shadow
	public abstract void begin(VertexFormat.Mode drawMode, VertexFormat vertexFormat);

	@Shadow
	public abstract void putShort(int i, short s);

	@Shadow
	public abstract void nextElement();

	@Override
	public void firis$beginWithoutExtending(VertexFormat.Mode drawMode, VertexFormat vertexFormat) {
		firis$shouldNotExtend = true;
		begin(drawMode, vertexFormat);
		firis$shouldNotExtend = false;
	}

	@Override
	public @NotNull VertexConsumer uv2(int pBufferVertexConsumer0, int pInt1) {
		return BufferVertexConsumer.super.uv2(pBufferVertexConsumer0, pInt1);
	}

	@ModifyVariable(method = "begin", at = @At("HEAD"), argsOnly = true)
	private VertexFormat firis$extendFormat(VertexFormat format) {
		extending = false;
		firis$isTerrain = false;
		injectNormalAndUV1 = false;

		if (firis$shouldNotExtend || !WorldRenderingSettings.INSTANCE.shouldUseExtendedVertexFormat()) {
			return format;
		}

		if (format == DefaultVertexFormat.BLOCK) {
			extending = true;
			firis$isTerrain = true;
			injectNormalAndUV1 = false;
			return FirisVertexFormats.TERRAIN;
		} else if (format == DefaultVertexFormat.NEW_ENTITY) {
			extending = true;
			firis$isTerrain = false;
			injectNormalAndUV1 = false;
			return FirisVertexFormats.ENTITY;
		} else if (format == DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP) {
			extending = true;
			firis$isTerrain = false;
			injectNormalAndUV1 = true;
			return FirisVertexFormats.GLYPH;
		}

		return format;
	}

	@Inject(method = "reset()V", at = @At("HEAD"))
	private void firis$onReset(CallbackInfo ci) {
		firis$vertexCount = 0;
	}

	@Inject(method = "endVertex", at = @At("HEAD"))
	private void firis$beforeNext(CallbackInfo ci) {
		if (!extending) {
			return;
		}

		if (injectNormalAndUV1 && currentElement == DefaultVertexFormat.ELEMENT_NORMAL) {
			this.putInt(0, 0);
			this.nextElement();
		}

		if (firis$isTerrain) {
			// ENTITY_ELEMENT
			this.putShort(0, currentBlock);
			this.putShort(2, currentRenderType);
		} else {
			// ENTITY_ID_ELEMENT
			this.putShort(0, (short) CapturedRenderingState.INSTANCE.getCurrentRenderedEntity());
			this.putShort(2, (short) CapturedRenderingState.INSTANCE.getCurrentRenderedBlockEntity());
			this.putShort(4, (short) CapturedRenderingState.INSTANCE.getCurrentRenderedItem());
		}

		this.nextElement();

		// MID_TEXTURE_ELEMENT
		this.putFloat(0, 0);
		this.putFloat(4, 0);
		this.nextElement();
		// TANGENT_ELEMENT
		this.putInt(0, 0);
		this.nextElement();
		if (firis$isTerrain) {
			// MID_BLOCK_ELEMENT
			int posIndex = this.nextElementByte - 48;
			float x = buffer.getFloat(posIndex);
			float y = buffer.getFloat(posIndex + 4);
			float z = buffer.getFloat(posIndex + 8);
			this.putInt(0, ExtendedDataHelper.computeMidBlock(x, y, z, currentLocalPosX, currentLocalPosY, currentLocalPosZ));
			this.nextElement();
		}

		firis$vertexCount++;

		if (mode == VertexFormat.Mode.QUADS && firis$vertexCount == 4 || mode == VertexFormat.Mode.TRIANGLES && firis$vertexCount == 3) {
			fillExtendedData(firis$vertexCount);
		}
	}

	@Unique
	private void fillExtendedData(int vertexAmount) {
		firis$vertexCount = 0;

		int stride = format.getVertexSize();

		polygon.setup(buffer, nextElementByte, stride, vertexAmount);

		float midU = 0;
		float midV = 0;

		for (int vertex = 0; vertex < vertexAmount; vertex++) {
			midU += polygon.u(vertex);
			midV += polygon.v(vertex);
		}

		midU /= vertexAmount;
		midV /= vertexAmount;

		int midUOffset;
		int midVOffset;
		int normalOffset;
		int tangentOffset;
		if (firis$isTerrain) {
			midUOffset = 16;
			midVOffset = 12;
			normalOffset = 24;
			tangentOffset = 8;
		} else {
			midUOffset = 14;
			midVOffset = 10;
			normalOffset = 24;
			tangentOffset = 6;
		}

		if (vertexAmount == 3) {
			// NormalHelper.computeFaceNormalTri(normal, polygon);	// Removed to enable smooth shaded triangles. Mods rendering triangles with bad normals need to recalculate their normals manually or otherwise shading might be inconsistent.

			for (int vertex = 0; vertex < vertexAmount; vertex++) {
				int packedNormal = buffer.getInt(nextElementByte - normalOffset - stride * vertex); // retrieve per-vertex normal

				int tangent = NormalHelper.computeTangentSmooth(NormI8.unpackX(packedNormal), NormI8.unpackY(packedNormal), NormI8.unpackZ(packedNormal), polygon);

				buffer.putFloat(nextElementByte - midUOffset - stride * vertex, midU);
				buffer.putFloat(nextElementByte - midVOffset - stride * vertex, midV);
				buffer.putInt(nextElementByte - tangentOffset - stride * vertex, tangent);
			}
		} else {
			NormalHelper.computeFaceNormal(normal, polygon);
			int packedNormal = NormI8.pack(normal.x, normal.y, normal.z, 0.0f);
			int tangent = NormalHelper.computeTangent(normal.x, normal.y, normal.z, polygon);

			for (int vertex = 0; vertex < vertexAmount; vertex++) {
				buffer.putFloat(nextElementByte - midUOffset - stride * vertex, midU);
				buffer.putFloat(nextElementByte - midVOffset - stride * vertex, midV);
				buffer.putInt(nextElementByte - normalOffset - stride * vertex, packedNormal);
				buffer.putInt(nextElementByte - tangentOffset - stride * vertex, tangent);
			}
		}
	}

	@Unique
	private void putInt(int i, int value) {
		this.buffer.putInt(this.nextElementByte + i, value);
	}

	@Override
	public void beginBlock(short block, short renderType, int localPosX, int localPosY, int localPosZ) {
		this.currentBlock = block;
		this.currentRenderType = renderType;
		this.currentLocalPosX = localPosX;
		this.currentLocalPosY = localPosY;
		this.currentLocalPosZ = localPosZ;
	}

	@Override
	public void endBlock() {
		this.currentBlock = -1;
		this.currentRenderType = -1;
		this.currentLocalPosX = 0;
		this.currentLocalPosY = 0;
		this.currentLocalPosZ = 0;
	}

	@Override
	public VertexFormat firis$format() {
		return format;
	}

	@Override
	public VertexFormat.Mode firis$mode() {
		return mode;
	}

	@Override
	public boolean firis$extending() {
		return extending;
	}

	@Override
	public boolean firis$isTerrain() {
		return firis$isTerrain;
	}

	@Override
	public boolean firis$injectNormalAndUV1() {
		return injectNormalAndUV1;
	}

	@Override
	public int firis$vertexCount() {
		return firis$vertexCount;
	}

	@Override
	public void firis$incrementVertexCount() {
		firis$vertexCount++;
	}

	@Override
	public void firis$resetVertexCount() {
		firis$vertexCount = 0;
	}

	@Override
	public short firis$currentBlock() {
		return currentBlock;
	}

	@Override
	public short firis$currentRenderType() {
		return currentRenderType;
	}

	@Override
	public int firis$currentLocalPosX() {
		return currentLocalPosX;
	}

	@Override
	public int firis$currentLocalPosY() {
		return currentLocalPosY;
	}

	@Override
	public int firis$currentLocalPosZ() {
		return currentLocalPosZ;
	}
}
