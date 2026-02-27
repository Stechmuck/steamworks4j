package com.codedisaster.steamworks;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class SteamInventory extends SteamInterface {

    public enum SteamItemFlags {
        NoTrade(1 << 0),
        Removed(1 << 8),
        Consumed(1 << 9);

        private final int bits;

        SteamItemFlags(final int bits) {
            this.bits = bits;
        }

        public static boolean isSet(final SteamItemFlags value, final int bitMask) {
            return (value.bits & bitMask) == value.bits;
        }
    }

    public static class SteamItemDetails {
        private long itemId;
        private int itemDefinition;
        private short quantity;
        private short flags;

        public SteamItemInstanceId getItemId() {
            return new SteamItemInstanceId(itemId);
        }

        public int getItemDefinition() {
            return itemDefinition;
        }

        public short getQuantity() {
            return quantity;
        }

        public short getFlags() {
            return flags;
        }
    }

    public SteamInventory(final SteamInventoryCallback callback) {
        setCallback(SteamInventoryNative.createCallback(new SteamInventoryCallbackAdapter(callback)));
    }

    public SteamResult getResultStatus(final SteamInventoryHandle inventory) {
        return SteamResult.byValue(SteamInventoryNative.getResultStatus(inventory.handle));
    }

    public int getResultItemsLength(final SteamInventoryHandle inventory) {
        return SteamInventoryNative.getResultItemsLength(inventory.handle);
    }

    public boolean getResultItems(final SteamInventoryHandle inventory, final List<SteamItemDetails> itemDetails) {
        final int itemCount = SteamInventoryNative.getResultItemsLength(inventory.handle);
        if (itemCount > 0) {
            final SteamItemDetails[] steamItemDetailsArray = new SteamItemDetails[itemCount];
            for (int i = 0; i < itemCount; i++) {
                steamItemDetailsArray[i] = new SteamItemDetails();
            }
            final boolean result = SteamInventoryNative.getResultItems(inventory.handle, steamItemDetailsArray);
            if (result) {
                itemDetails.addAll(Arrays.stream(steamItemDetailsArray).collect(Collectors.toList()));
            }
            return result;
        }
        return false;
    }

    public String getResultItemPropertyKeys(final SteamInventoryHandle inventory, final int itemIndex) {
        return SteamInventoryNative.getResultItemPropertyKeys(inventory.handle, itemIndex);
    }

    public boolean getResultItemProperty(final SteamInventoryHandle inventory, final int itemIndex, final String propertyName, final List<String> values) {
        final SteamStringValue steamValue = new SteamStringValue();
        final boolean result = SteamInventoryNative.getResultItemProperty(inventory.handle, itemIndex, propertyName, steamValue);
        values.add(steamValue.getValue());
        return result;
    }

    public int getResultTimestamp(final SteamInventoryHandle inventory) {
        return SteamInventoryNative.getResultTimestamp(inventory.handle);
    }

    public boolean checkResultSteamID(final SteamInventoryHandle inventory, final SteamID steamIDExpected) {
        return SteamInventoryNative.checkResultSteamID(inventory.handle, steamIDExpected.handle);
    }

    public void destroyResult(final SteamInventoryHandle inventory) {
        SteamInventoryNative.destroyResult(inventory.handle);
    }

    public boolean getAllItems(final List<SteamInventoryHandle> inventories) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.getAllItems(tempIntArray);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean getItemsByID(final List<SteamInventoryHandle> inventories, final List<SteamItemInstanceId> instanceIDs) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.getItemsByID(tempIntArray,
                instanceIDs.stream().mapToLong(id -> id.handle).toArray(), instanceIDs.size());
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public int getSizeNeededForResultSerialization(final SteamInventoryHandle inventory) {
        return SteamInventoryNative.serializeResultSize(inventory.handle);
    }

    public boolean serializeResult(final SteamInventoryHandle inventory, final ByteBuffer outBuffer) throws SteamException {
        checkBuffer(outBuffer);
        return SteamInventoryNative.serializeResult(inventory.handle, outBuffer, outBuffer.position(), outBuffer.remaining());
    }

    public boolean deserializeResult(final List<SteamInventoryHandle> inventories, final ByteBuffer buffer) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.deserializeResult(tempIntArray, buffer, buffer.position(), buffer.remaining(), false);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean generateItems(final List<SteamInventoryHandle> inventories, final int[] arrayItemDefs, final int[] arrayQuantity) {
        if (arrayItemDefs.length != arrayQuantity.length) {
            throw new IllegalArgumentException("The length of arrayItemDefs and arrayQuantity must match!");
        }
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.generateItems(tempIntArray, arrayItemDefs, arrayQuantity, arrayItemDefs.length);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean grantPromoItems(final List<SteamInventoryHandle> inventories) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.grantPromoItems(tempIntArray);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean addPromoItem(final List<SteamInventoryHandle> inventories, final int itemDef) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.addPromoItem(tempIntArray, itemDef);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean addPromoItems(final List<SteamInventoryHandle> inventories, final int[] arrayItemDefs) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.addPromoItems(tempIntArray, arrayItemDefs, arrayItemDefs.length);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean consumeItem(final List<SteamInventoryHandle> inventories, final SteamItemInstanceId itemConsume, final int quantity) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.consumeItem(tempIntArray, itemConsume.handle, quantity);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean exchangeItems(final List<SteamInventoryHandle> inventories, final int[] arrayGenerate, final int[] arrayGenerateQuantity,
                                 final int arrayGenerateLength, final SteamItemInstanceId[] arrayDestroy,
                                 final int[] arrayDestroyQuantity, final int arrayDestroyLength) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.exchangeItems(tempIntArray, arrayGenerate, arrayGenerateQuantity, arrayGenerateLength,
                Arrays.stream(arrayDestroy).mapToLong(id -> id.handle).toArray(), arrayDestroyQuantity, arrayDestroyLength);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean transferItemQuantity(final List<SteamInventoryHandle> inventories, final SteamItemInstanceId itemIdSource,
                                        final int quantity, final SteamItemInstanceId itemIdDest) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.transferItemQuantity(tempIntArray, itemIdSource.handle, quantity, itemIdDest.handle);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    @Deprecated
    public void sendItemDropHeartbeat() {
        SteamInventoryNative.sendItemDropHeartbeat();
    }

    public boolean triggerItemDrop(final List<SteamInventoryHandle> inventories, final int dropListDefinition) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.triggerItemDrop(tempIntArray, dropListDefinition);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean loadItemDefinitions() {
        return SteamInventoryNative.loadItemDefinitions();
    }

    public boolean getItemDefinitionIDs(final List<Integer> itemDefIDs) {
        final int size = SteamInventoryNative.getItemDefinitionIDSize();
        final int[] tempIntArray = new int[size];
        final boolean result = SteamInventoryNative.getItemDefinitionIDs(tempIntArray, size);
        if (result) {
            itemDefIDs.addAll(Arrays.stream(tempIntArray).boxed().collect(Collectors.toList()));
        }
        return result;
    }

    public String getItemDefinitionPropertyKeys(final int itemDefinition) {
        return SteamInventoryNative.getItemDefinitionPropertyKeys(itemDefinition);
    }

    public boolean getItemDefinitionProperty(final int itemDefinition, final String propertyName, final List<String> values) {
        final SteamStringValue steamValue = new SteamStringValue();
        final boolean result = SteamInventoryNative.getItemDefinitionProperty(itemDefinition, propertyName, steamValue);
        values.add(steamValue.getValue());
        return result;
    }

    public SteamAPICall requestEligiblePromoItemDefinitionsIDs(final SteamID steamID) {
        return new SteamAPICall(SteamInventoryNative.requestEligiblePromoItemDefinitionsIDs(callback, steamID.handle));
    }

    public boolean getEligiblePromoItemDefinitionIDs(final SteamID steamID, final List<Integer> itemDefIDs, final int size) {
        final int[] tempIntArray = new int[size];
        Arrays.fill(tempIntArray, -1);
        final boolean result = SteamInventoryNative.getEligiblePromoItemDefinitionIDs(steamID.handle, tempIntArray, size);
        if (result) {
            itemDefIDs.addAll(Arrays.stream(tempIntArray).boxed().collect(Collectors.toList()));
        }
        return result;
    }

    public SteamAPICall startPurchase(final int[] arrayItemDefs, final int[] arrayQuantity) {
        return new SteamAPICall(SteamInventoryNative.startPurchase(callback, arrayItemDefs, arrayQuantity, arrayItemDefs.length));
    }

    public SteamAPICall requestPrices() {
        return new SteamAPICall(SteamInventoryNative.requestPrices(callback));
    }

    public int getNumItemsWithPrices() {
        return SteamInventoryNative.getNumItemsWithPrices();
    }

    public boolean getItemsWithPrices(final int[] arrayItemDefs, final long[] currentPrices, final long[] basePrices) {
        return SteamInventoryNative.getItemsWithPrices(arrayItemDefs, currentPrices, basePrices, arrayItemDefs.length);
    }

    public boolean getItemPrice(final int itemDefinition, final long[] currentPrice, final long[] basePrice) {
        return SteamInventoryNative.getItemPrice(itemDefinition, currentPrice, basePrice);
    }

    public SteamInventoryUpdateHandle startUpdateProperties() {
        return new SteamInventoryUpdateHandle(SteamInventoryNative.startUpdateProperties());
    }

    public boolean removeProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName) {
        return SteamInventoryNative.removeProperty(updateHandle.handle, itemID.handle, propertyName);
    }

    public boolean setProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName, final String value) {
        return SteamInventoryNative.setProperty(updateHandle.handle, itemID.handle, propertyName, value);
    }

    public boolean setProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName, final boolean value) {
        return SteamInventoryNative.setProperty(updateHandle.handle, itemID.handle, propertyName, value);
    }

    public boolean setProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName, final long value) {
        return SteamInventoryNative.setProperty(updateHandle.handle, itemID.handle, propertyName, value);
    }

    public boolean setProperty(final SteamInventoryUpdateHandle updateHandle, final SteamItemInstanceId itemID, final String propertyName, final float value) {
        return SteamInventoryNative.setProperty(updateHandle.handle, itemID.handle, propertyName, value);
    }

    public boolean submitUpdateProperties(final SteamInventoryUpdateHandle updateHandle, final List<SteamInventoryHandle> inventories) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.submitUpdateProperties(updateHandle.handle, tempIntArray);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

    public boolean inspectItem(final List<SteamInventoryHandle> inventories, final String itemToken) {
        final int[] tempIntArray = new int[1];
        final boolean result = SteamInventoryNative.inspectItem(tempIntArray, itemToken);
        if (result) {
            inventories.addAll(SteamInventoryHandle.mapToHandles(tempIntArray));
        }
        return result;
    }

}
