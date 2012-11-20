package com.gentics.kitchenoffice.webapp.container.util;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * Watches one or more {@link Item} objects, and notifies all registered
 * {@link ItemChangedListener} of changes in value.
 */
public class Watcher implements Serializable {

	protected Collection<ItemChangedListener> listeners = new LinkedList<ItemChangedListener>();

	protected Map<Item, ItemWatcher> itemWatchers = new HashMap<Item, ItemWatcher>();

	/**
	 * Registers a new ItemChangeListener
	 * 
	 * @param listener
	 *            the listener to be added.
	 */
	public void addListener(ItemChangedListener listener) {
		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes a previously registered ItemChangeListener
	 * 
	 * @param listener
	 *            the listener to be removed.
	 */
	public void removeListener(ItemChangedListener listener) {
		if (listener != null && listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	/**
	 * Notifies all ItemChangeListeners of an ItemChangedEvent
	 * 
	 * @param event
	 *            The event to notify listeners of
	 */
	protected void notifyItemChanged(ItemChangedEvent event) {
		for (ItemChangedListener listener : listeners) {
			listener.itemChanged(event);
		}
	}

	/**
	 * Watches an item for all property changes, and notifies all registered
	 * ItemChangeListeners of any value change.
	 * 
	 * @param item
	 *            The item to watch
	 */
	public void watch(Item item) {
		ItemWatcher watcher = new ItemWatcher(item);
		watcher.watch();
		itemWatchers.put(item, watcher);
	}

	/**
	 * Stops watching the given item, removing any listeners
	 * 
	 * @param item
	 *            The item to stop watching
	 */
	protected void unwatch(Item item) {
		ItemWatcher itemWatcher = itemWatchers.remove(item);
		if (itemWatcher != null) {
			itemWatcher.unwatch();
		}
	}

	/**
	 * Watches a single item, and
	 */
	protected class ItemWatcher implements Property.ValueChangeListener {
		private static final long serialVersionUID = -5521598324121258216L;

		protected Collection<Object> watchedPropertyIds = new HashSet<Object>();
		protected Item item;

		public ItemWatcher(Item item) {
			this.item = item;
		}

		/**
		 * Watch all of the properties on the item
		 */
		public void watch() {
			watch(item.getItemPropertyIds());
		}

		/**
		 * Watches the given properties on the item
		 * 
		 * @param propertyIds
		 *            the ids of the properties to watch
		 */
		public void watch(Iterable<?> propertyIds) {
			for (Object propertyId : propertyIds) {
				watch(propertyId);
			}
		}

		/**
		 * Attempts to watch the given property on the item
		 * 
		 * @param propertyId
		 *            the id pof the property to watch
		 */
		public void watch(Object propertyId) {
			Property<?> property = item.getItemProperty(propertyId);
			if (property instanceof Property.ValueChangeNotifier) {
				((Property.ValueChangeNotifier) property).addValueChangeListener(this);
				watchedPropertyIds.add(propertyId);
			}
		}

		/**
		 * Stops watching all of the properties on the item
		 */
		public void unwatch() {
			unwatch(new HashSet<Object>(watchedPropertyIds));
		}

		/**
		 * Stops watching the given properties on the item
		 * 
		 * @param propertyIds
		 *            The ids of the properties to stop watching
		 */
		public void unwatch(Iterable<?> propertyIds) {
			for (Object propertyId : propertyIds) {
				unwatch(propertyId);
			}
		}

		/**
		 * Stops watching the given property
		 * 
		 * @param propertyId
		 *            the id of the property to stop watching
		 */
		public void unwatch(Object propertyId) {
			if (watchedPropertyIds.contains(propertyId)) {
				Property<?> property = item.getItemProperty(propertyId);
				if (property instanceof Property.ValueChangeNotifier) {
					((Property.ValueChangeNotifier) property)
							.removeValueChangeListener(this);
					watchedPropertyIds.remove(propertyId);
				}
			}
		}

		public void valueChange(Property.ValueChangeEvent event) {
			notifyItemChanged(new ItemChangedEvent(item, event.getProperty()));
		}
	}
}