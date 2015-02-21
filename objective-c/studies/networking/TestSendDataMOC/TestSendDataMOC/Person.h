//
//  Person.h
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/16/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Person : NSManagedObject

@property (nonatomic, retain) NSNumber * iD;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic, retain) NSString * firstName;

+(id)entityName;
+(NSEntityDescription *)entity:(NSManagedObjectContext *)context;
+(Person *)newPerson:(NSManagedObjectContext *)context;

+(NSData *)toJSON:(NSNumber *)iD firstName:(NSString *)firstName andLastName:(NSString *)lastName;
-(NSData *)toJSON;
-(void)parseFromJSON:(NSData *)data;
-(void)parseFromDictionary:(NSDictionary *)dictionary;

@end
