package com.codedisaster.steamworks;

import java.nio.ByteBuffer;

final class SteamInventoryNative {

	// @off

	/*JNI
		#include "SteamInventoryCallback.h"
		#include <vector>
	*/

	static native long createCallback(SteamInventoryCallbackAdapter javaCallback); /*
		return (intp) new SteamInventoryCallback(env, javaCallback);
	*/

	static native int getResultStatus(int resultHandle); /*
		return SteamInventory()->GetResultStatus((SteamInventoryResult_t) resultHandle);
	*/

	static native int getResultItemsLength(int resultHandle); /*
		uint32 count = 0;
		bool success = SteamInventory()->GetResultItems((SteamInventoryResult_t) resultHandle, NULL, &count);
		if(success) {
			return count;
		}
		return -1;
	*/

	static native boolean getResultItems(int resultHandle, SteamInventory.SteamItemDetails[] itemDetails); /*
		uint32 count = 0;
		bool success = false;
		if(SteamInventory()->GetResultItems((SteamInventoryResult_t) resultHandle, NULL, &count)) {
			std::vector<SteamItemDetails_t> results;
			results.resize(count);
			success = SteamInventory()->GetResultItems((SteamInventoryResult_t) resultHandle, results.data(), &count);
			if (success) {
				for(unsigned int a = 0; a < count; a = a + 1) {
					jclass clazz = env->GetObjectClass(env->GetObjectArrayElement(itemDetails, a));
					jfieldID field = env->GetFieldID(clazz, "itemId", "J");
					env->SetLongField(env->GetObjectArrayElement(itemDetails, a), field, (jlong) results[a].m_itemId);
					field = env->GetFieldID(clazz, "itemDefinition", "I");
					env->SetIntField(env->GetObjectArrayElement(itemDetails, a), field, (jint) results[a].m_iDefinition);
					field = env->GetFieldID(clazz, "quantity", "S");
					env->SetShortField(env->GetObjectArrayElement(itemDetails, a), field, (jshort) results[a].m_unQuantity);
					field = env->GetFieldID(clazz, "flags", "S");
					env->SetShortField(env->GetObjectArrayElement(itemDetails, a), field, (jshort) results[a].m_unFlags);
				}
			}
		}
		return success;
	*/

	static native String getResultItemPropertyKeys(int resultHandle, int itemIndex); /*
		char *valueBuffer = (char*) malloc(1);
		uint32 valueBufferSizeOut = 0;
		SteamInventory()->GetResultItemProperty((SteamInventoryResult_t) resultHandle, itemIndex, NULL, valueBuffer, &valueBufferSizeOut);
		valueBuffer = (char*) malloc(valueBufferSizeOut);
		SteamInventory()->GetResultItemProperty((SteamInventoryResult_t) resultHandle, itemIndex, NULL, valueBuffer, &valueBufferSizeOut);
		return env->NewStringUTF(valueBuffer);
	*/

	static native boolean getResultItemProperty(int resultHandle, int itemIndex, String propertyName, SteamStringValue value); /*
		char *valueBuffer = (char*) malloc(1);
		uint32 valueBufferSizeOut = 0;
		SteamInventory()->GetResultItemProperty((SteamInventoryResult_t) resultHandle, itemIndex, propertyName, valueBuffer, &valueBufferSizeOut);
		valueBuffer = (char*) malloc(valueBufferSizeOut);
		bool success = SteamInventory()->GetResultItemProperty((SteamInventoryResult_t) resultHandle, itemIndex, propertyName, valueBuffer, &valueBufferSizeOut);
		jclass valueClazz = env->GetObjectClass(value);
		jfieldID field = env->GetFieldID(valueClazz, "value", "Ljava/lang/String;");
		env->SetObjectField(value, field, env->NewStringUTF(valueBuffer));
		return success;
	*/

	static native int getResultTimestamp(int resultHandle); /*
		return SteamInventory()->GetResultTimestamp((SteamInventoryResult_t) resultHandle);
	*/

	static native boolean checkResultSteamID(int resultHandle, long steamIDExpected); /*
		return SteamInventory()->CheckResultSteamID((SteamInventoryResult_t) resultHandle, (uint64) steamIDExpected);
	*/

	static native void destroyResult(int resultHandle); /*
		SteamInventory()->DestroyResult((SteamInventoryResult_t) resultHandle);
	*/

	static native boolean getAllItems(int[] resultHandles); /*
		return SteamInventory()->GetAllItems((SteamInventoryResult_t*) resultHandles);
	*/

	static native boolean getItemsByID(int[] resultHandles, long[] instanceIDs, int countInstanceIDs); /*
		return SteamInventory()->GetItemsByID((SteamInventoryResult_t*) resultHandles, (SteamItemInstanceID_t*) instanceIDs, countInstanceIDs);
	*/

	static native int serializeResultSize(int resultHandle); /*
		uint32 outBufferSize = 0;
		bool success = SteamInventory()->SerializeResult((SteamInventoryResult_t) resultHandle, NULL, &outBufferSize);
		if(success) {
			return outBufferSize;
		} else {
			return -1;
		}
	*/

	static native boolean serializeResult(int resultHandle, ByteBuffer outBuffer, int offset, int outBufferSize); /*
		return SteamInventory()->SerializeResult((SteamInventoryResult_t) resultHandle, &outBuffer[offset], (uint32*) &outBufferSize);
	*/

	static native boolean deserializeResult(int[] resultHandles, ByteBuffer buffer, int offset, int bufferSize, boolean reserved); /*
		return SteamInventory()->DeserializeResult((SteamInventoryResult_t*) resultHandles, &buffer[offset], bufferSize, reserved);
	*/

	static native boolean generateItems(int[] resultHandles, int[] arrayItemDefs, int[] arrayQuantity, int arrayLength); /*
		return SteamInventory()->GenerateItems((SteamInventoryResult_t*) resultHandles, (SteamItemDef_t*) arrayItemDefs, (uint32*) arrayQuantity, arrayLength);
	*/

	static native boolean grantPromoItems(int[] resultHandles); /*
		return SteamInventory()->GrantPromoItems((SteamInventoryResult_t*) resultHandles);
	*/

	static native boolean addPromoItem(int[] resultHandles, int itemDef); /*
		return SteamInventory()->AddPromoItem((SteamInventoryResult_t*) resultHandles, itemDef);
	*/

	static native boolean addPromoItems(int[] resultHandles, int[] arrayItemDefs, int arrayLength); /*
		return SteamInventory()->AddPromoItems((SteamInventoryResult_t*) resultHandles, (SteamItemDef_t*) arrayItemDefs, arrayLength);
	*/

	static native boolean consumeItem(int[] resultHandles, long itemConsume, int quantity); /*
		return SteamInventory()->ConsumeItem((SteamInventoryResult_t*) resultHandles, (SteamItemInstanceID_t) itemConsume, quantity);
	*/

	static native boolean exchangeItems(int[] resultHandles, int[] arrayGenerate, int[] arrayGenerateQuantity, int arrayGenerateLength,
	                                    long[] arrayDestroy, int[] arrayDestroyQuantity, int arrayDestroyLength); /*
		return SteamInventory()->ExchangeItems((SteamInventoryResult_t*) resultHandles, (SteamItemDef_t*) arrayGenerate, (uint32*) arrayGenerateQuantity, arrayGenerateLength, (SteamItemInstanceID_t*) arrayDestroy, (uint32*) arrayDestroyQuantity, arrayDestroyLength);
	*/

	static native boolean transferItemQuantity(int[] resultHandles, long itemIdSource, int quantity, long itemIdDest); /*
		return SteamInventory()->TransferItemQuantity((SteamInventoryResult_t*) resultHandles, (SteamItemInstanceID_t) itemIdSource, quantity, (SteamItemInstanceID_t) itemIdDest);
	*/

	static native void sendItemDropHeartbeat(); /*
		SteamInventory()->SendItemDropHeartbeat();
	*/

	static native boolean triggerItemDrop(int[] resultHandles, int dropListDefinition); /*
		return SteamInventory()->TriggerItemDrop((SteamInventoryResult_t*) resultHandles, dropListDefinition);
	*/

	static native boolean loadItemDefinitions(); /*
		return SteamInventory()->LoadItemDefinitions();
	*/

	static native int getItemDefinitionIDSize(); /*
		uint32 count = 0;
		bool success = SteamInventory()->GetItemDefinitionIDs(NULL, &count);
		if(success) {
			return count;
		}
		return -1;
	*/

	static native boolean getItemDefinitionIDs(int[] itemDefIDs, int itemDefIDsArraySize); /*
		return SteamInventory()->GetItemDefinitionIDs((SteamItemDef_t*) itemDefIDs, (uint32*) &itemDefIDsArraySize);
	*/

	static native String getItemDefinitionPropertyKeys(int itemDefinition); /*
		char *valueBuffer = (char*) malloc(1);
		uint32 valueBufferSizeOut = 0;
		SteamInventory()->GetItemDefinitionProperty((SteamItemDef_t) itemDefinition, NULL, valueBuffer, &valueBufferSizeOut);
		valueBuffer = (char*) malloc(valueBufferSizeOut);
		SteamInventory()->GetItemDefinitionProperty((SteamItemDef_t) itemDefinition, NULL, valueBuffer, &valueBufferSizeOut);
		return env->NewStringUTF(valueBuffer);
	*/

	static native boolean getItemDefinitionProperty(int itemDefinition, String propertyName, SteamStringValue value); /*
		char *valueBuffer = (char*) malloc(1);
		uint32 valueBufferSizeOut = 0;
		SteamInventory()->GetItemDefinitionProperty((SteamItemDef_t) itemDefinition, propertyName, valueBuffer, &valueBufferSizeOut);
		valueBuffer = (char*) malloc(valueBufferSizeOut);
		bool success = SteamInventory()->GetItemDefinitionProperty((SteamItemDef_t) itemDefinition, propertyName, valueBuffer, &valueBufferSizeOut);
		jclass valueClazz = env->GetObjectClass(value);
		jfieldID field = env->GetFieldID(valueClazz, "value", "Ljava/lang/String;");
		env->SetObjectField(value, field, env->NewStringUTF(valueBuffer));
		return success;
	*/

	static native long requestEligiblePromoItemDefinitionsIDs(long callback, long steamID); /*
		SteamInventoryCallback* cb = (SteamInventoryCallback*) callback;
		SteamAPICall_t handle = SteamInventory()->RequestEligiblePromoItemDefinitionsIDs(CSteamID((uint64) steamID));
		cb->onSteamInventoryEligiblePromoItemDefIDsCall.Set(handle, cb, &SteamInventoryCallback::onSteamInventoryEligiblePromoItemDefIDs);
		return handle;
	*/

	static native boolean getEligiblePromoItemDefinitionIDs(long steamID, int[] itemDefIDs, int itemDefIDsArraySize); /*
		return SteamInventory()->GetEligiblePromoItemDefinitionIDs((uint64) steamID, (SteamItemDef_t*) itemDefIDs, (uint32*) &itemDefIDsArraySize);
	*/

	static native long startPurchase(long callback, int[] arrayItemDefs, int[] arrayQuantity, int arrayLength); /*
		SteamInventoryCallback* cb = (SteamInventoryCallback*) callback;
		SteamAPICall_t handle = SteamInventory()->StartPurchase((SteamItemDef_t*) arrayItemDefs, (uint32*) arrayQuantity, arrayLength);
		cb->onSteamInventoryStartPurchaseResultCall.Set(handle, cb, &SteamInventoryCallback::onSteamInventoryStartPurchaseResult);
		return handle;
	*/

	static native long requestPrices(long callback); /*
		SteamInventoryCallback* cb = (SteamInventoryCallback*) callback;
		SteamAPICall_t handle = SteamInventory()->RequestPrices();
		cb->onSteamInventoryRequestPricesResultCall.Set(handle, cb, &SteamInventoryCallback::onSteamInventoryRequestPricesResult);
		return handle;
	*/

	static native int getNumItemsWithPrices(); /*
		return SteamInventory()->GetNumItemsWithPrices();
	*/

	static native boolean getItemsWithPrices(int[] arrayItemDefs, long[] currentPrices, long[] basePrices, int arrayLength); /*
		return SteamInventory()->GetItemsWithPrices((SteamItemDef_t*) arrayItemDefs, (uint64*) currentPrices, (uint64*) basePrices, arrayLength);
	*/

	static native boolean getItemPrice(int itemDefinition, long[] currentPrice, long[] basePrice); /*
		return SteamInventory()->GetItemPrice((SteamItemDef_t) itemDefinition, (uint64*) currentPrice, (uint64*) basePrice);
	*/

	static native long startUpdateProperties(); /*
		return SteamInventory()->StartUpdateProperties();
	*/

	static native boolean removeProperty(long updateHandle, long itemID, String propertyName); /*
		return SteamInventory()->RemoveProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName);
	*/

	static native boolean setProperty(long updateHandle, long itemID, String propertyName, String value); /*
		return SteamInventory()->SetProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName, (char*) value);
	*/

	static native boolean setProperty(long updateHandle, long itemID, String propertyName, boolean value); /*
		return SteamInventory()->SetProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName, (bool) value);
	*/

	static native boolean setProperty(long updateHandle, long itemID, String propertyName, long value); /*
		return SteamInventory()->SetProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName, (int64) value);
	*/

	static native boolean setProperty(long updateHandle, long itemID, String propertyName, float value); /*
		return SteamInventory()->SetProperty((SteamInventoryUpdateHandle_t) updateHandle, (SteamItemInstanceID_t) itemID, propertyName, (float) value);
	*/

	static native boolean submitUpdateProperties(long updateHandle, int[] resultHandles); /*
		return SteamInventory()->SubmitUpdateProperties((SteamInventoryUpdateHandle_t) updateHandle, (SteamInventoryResult_t*) resultHandles);
	*/

	static native boolean inspectItem(int[] resultHandles, String itemToken); /*
		return SteamInventory()->InspectItem((SteamInventoryResult_t*) resultHandles, itemToken);
	*/

}
