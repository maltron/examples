//
//  AppDelegate.h
//  TestEnumeration
//
//  Created by Mauricio Leal on 2/26/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *mainContext;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;


@end

