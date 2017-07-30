package de.weltraumschaf.maconha.ui.view.user;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;
import de.weltraumschaf.maconha.app.BeanLocator;
import de.weltraumschaf.maconha.backend.model.entity.User;
import de.weltraumschaf.maconha.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import org.vaadin.spring.annotation.PrototypeScope;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@PrototypeScope
public class UserAdminDataProvider extends FilterablePageableDataProvider<User, Object> {

    private final BeanLocator locator;
    private transient UserService userService;

    @Autowired
    public UserAdminDataProvider(final BeanLocator locator) {
        super();
        this.locator = locator;
    }

    @Override
    protected Page<User> fetchFromBackEnd(Query<User, Object> query, Pageable pageable) {
        return getUserService().findAnyMatching(getOptionalFilter(), pageable);
    }

    @Override
    protected int sizeInBackEnd(Query<User, Object> query) {
        return (int) getUserService().countAnyMatching(getOptionalFilter());
    }

    protected UserService getUserService() {
        if (userService == null) {
            userService = locator.find(UserService.class);
        }
        return userService;
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        List<QuerySortOrder> sortOrders = new ArrayList<>();
        sortOrders.add(new QuerySortOrder("email", SortDirection.ASCENDING));
        return sortOrders;
    }

}