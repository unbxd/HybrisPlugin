package com.unbxd.backoffice.widgets.actions;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;
import de.hybris.platform.core.model.ItemModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UnbxdProductSyncActionTest extends AbstractActionUnitTest<UnbxdProductSyncAction>
{

    @Mock
    private ObjectFacade objectFacade;

    @InjectMocks
    UnbxdProductSyncAction action = new UnbxdProductSyncAction();

    @Override
    public UnbxdProductSyncAction getActionInstance()
    {
        return action;
    }

    @Test
    public void testCannotPerformWithNull()
    {
        final ActionContext<Object> ctx = mock(ActionContext.class);
        when(ctx.getData()).thenReturn(null);

        assertThat(action.canPerform(ctx)).isFalse();
        verify(objectFacade, never()).isModified(null);
    }

    @Test
    public void testCannotPerformWithListOfNull()
    {
        final ActionContext<Object> ctx = mock(ActionContext.class);
        final List<Object> items = new ArrayList<>();
        items.add(null);
        when(ctx.getData()).thenReturn(items);

        assertThat(action.canPerform(ctx)).isFalse();
        verify(objectFacade, never()).isModified(null);
    }

    @Test
    public void testCannotPerformWithListOfModifiedItems()
    {
        final ItemModel itemModel = mock(ItemModel.class);
        doReturn(Boolean.TRUE).when(objectFacade).isModified(itemModel);
        final ActionContext<Object> ctx = mock(ActionContext.class);
        when(ctx.getData()).thenReturn(Lists.newArrayList(itemModel));

        assertThat(action.canPerform(ctx)).isFalse();
    }

    @Test
    public void testCannotPerformWithOneModifiedItem()
    {
        final ItemModel itemModel = mock(ItemModel.class);
        doReturn(Boolean.TRUE).when(objectFacade).isModified(itemModel);
        final ActionContext<Object> ctx = mock(ActionContext.class);
        when(ctx.getData()).thenReturn(itemModel);

        assertThat(action.canPerform(ctx)).isFalse();
    }

    @Test
    public void testCanPerformWitListOfItems()
    {
        final ItemModel itemModel = mock(ItemModel.class);
        doReturn(Boolean.FALSE).when(objectFacade).isModified(itemModel);
        final ActionContext<Object> ctx = mock(ActionContext.class);
        when(ctx.getData()).thenReturn(Lists.newArrayList(itemModel));

        assertThat(action.canPerform(ctx)).isTrue();
    }

    @Test
    public void testCanPerformOneItem()
    {
        final ItemModel itemModel = mock(ItemModel.class);
        doReturn(Boolean.FALSE).when(objectFacade).isModified(itemModel);
        final ActionContext<Object> ctx = mock(ActionContext.class);
        when(ctx.getData()).thenReturn(itemModel);

        assertThat(action.canPerform(ctx)).isTrue();
    }
}
